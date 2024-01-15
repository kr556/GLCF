package org.glcf2.component.glcomponent;

import org.glcf2.Drawing;
import org.glcf2.Shader;
import org.glcf2.UniformNames;
import org.glcf2.component.*;
import org.glcf2.component.Component;
import org.glcf2.programobject.IBO;
import org.glcf2.programobject.VBO;
import org.glcf2.models.Model;
import org.glcf2.models.GLVBOModel;
import org.glcf2.shaders.GLSL;
import org.glcf2.vertex.ArrayModelFactory;
import org.linear.main.matrix.Matrix4f;
import org.linear.main.vector.Vector2d;
import org.linear.main.vector.Vector2i;
import org.linear.main.vector.Vector3i;
import org.linear.main.vector.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.glcf2.FragUtil32.*;
import static org.glcf2.component.glcomponent.GLEventID.*;

//TODO 未実装
public abstract class GLAbsComponent implements Component, MouseEvent {
    static {
        var arrM = ArrayModelFactory.createModel(new Vector4f[]{
                new Vector4f(1, 1, 1, 1),
                new Vector4f(1, 0, 1, 1),
                new Vector4f(0, 0, 1, 1),
                new Vector4f(0, 1, 1, 1)
        });

        var idx = IBO.create(new Vector3i[]{
                new Vector3i(0, 1, 2),
                new Vector3i(2, 3, 0)
        }, 3);

        GLVBOModel re = new GLVBOModel(VBO.create(arrM.getVerticies(), 3), idx, GLSL.read("glcf/component/glsl/button"));
        re.setColor(VBO.create(new Vector4f[]{
                new Vector4f(0, 0, 0, 1),
                new Vector4f(0, 0, 0, 1),
                new Vector4f(0, 0, 0, 1),
                new Vector4f(0, 0, 0, 1)
        }, 4));

        rec = re;
    }

    private static final Model rec;

    private GLWindow root;

    private Component parent;
    private String name;
    private Vector2d size;
    private Vector2d pos;
    private Vector2d pivot;
    private Vector4f background;
    private List<Component> components = new ArrayList<>();
    private Model model;
    private Mouse mouse;
    private Keybord keybord;
    private Joystick joystick;
    private int layer;

    private transient Vector2d tmp2v = new Vector2d();
    private transient Matrix4f tmp4m = Matrix4f.DIAGONAL.clone();
    private transient int mouseFrag;

    {
        background = new Vector4f();
        size = new Vector2d();
        pos = new Vector2d();
        pivot = new Vector2d();
        model = rec.clone();
        layer = 0;
    }

    public GLAbsComponent(Component parent) {
        this.parent = parent;
        this.root = (GLWindow) parent.getRoot();
        this.mouse = root.getEvent().getMouse();
        this.keybord = root.getEvent().getKeybord();
        this.joystick = root.getEvent().getJoystick();
        this.name = "";

        this.model.getShader().setUniform(UniformNames.LAYER, layer);
    }

    @Override
    public boolean mouseHit() {
        Vector2d p = mouse.getPos();

        if (p.isNaN()) return false;

        return (p.x >= pos.x && p.x <= pos.x + size.x) &&
               (p.y >= pos.y && p.y <= pos.y + size.y);
    }

    //FIXME レイヤーが何故か働かない
    @Override
    public void drawing() {
        tmp4m.set(Matrix4f.DIAGONAL);

        tmp4m.translate((float) pos.x, (float) pos.y, -(layer * 0.1f) * Float.MIN_VALUE);
//        System.out.printf("%s : %d : %54.50f\n", name, layer, -(root.maxLayer - layer) * Float.MIN_VALUE);

        tmp4m.scale((float) size.x, (float) size.y, 1);

        model.getShader().setUniformm(UniformNames.MAT4, tmp4m.toNewArray());
        model.getShader().setUniformvf(UniformNames.BACKGROUND, background);

        if (model != null) model.drawing();
        if (!components.isEmpty()) components.forEach(Drawing::drawing);
    }

    @Override
    public void setShader(Shader shader) {
        model.setShader(shader);
    }

    @Override
    public void setSize(Vector2i size) {}

    @Override
    public void setSize(Vector2d size) {
        this.size.set(size);
    }

    @Override
    public org.glcf2.component.Window getRoot() {
        return root;
    }

    @Override
    public void setPos(Vector2i pos) {}

    @Override
    public void setPos(Vector2d pos) {
        this.pos.set(pos);
    }

    @Override
    public void add(Component[] parts) {
        components.addAll(Arrays.asList(parts));
    }

    @Override
    public void add(Component part) {
        if (part instanceof GLAbsComponent _part) {
            _part.layer = this.layer + 1;

            if (root.maxLayer < _part.layer) root.maxLayer++;
        }

        components.add(part);
    }

    @Override
    public Component get(int index) {
        return components.get(index);
    }

    @Override
    public Component[] getAll() {
        return components.toArray(new Component[0]);
    }

    @Override
    public Vector2d getPos() {
        return pos;
    }

    @Override
    public Vector2d getSize() {
        return size;
    }

    @Override
    public void remove(Predicate<Component> filter) {
        components = components.stream()
                .filter(filter)
                .toList();
    }

    @Override
    public void removeAll() {
        components.clear();
    }

    @Override
    public Vector2i getAbsPos() {
        return null;
    }

    @Override
    public Vector2i getAbsSize() {
        return null;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public Shader getShader() {
        return model.getShader();
    }

    @Override
    public void invokeMouseAction() {
        if (fragIs(mouseFrag, MOUSE_IN)) { // mouse inside action.
            mouseHitting(mouse);

            if (mouse.getEvent() == event.press()) { // pressed action.
                mousePressed(mouse);
                mouseFrag = fragSetTrue(mouseFrag, MOUSE_PRESS);
            } else if (mouse.getEvent() == event.release()) { // released action.
                mouseReleased(mouse);

                if (fragIs(mouseFrag, MOUSE_PRESS)) mouseClicked(mouse); // clicked action.

                mouseFrag = fragSetFalse(mouseFrag, MOUSE_PRESS);
            }

            if (!mouseHit()) {
                mouseExited(mouse);
                mouseFrag = fragSetFalse(mouseFrag, MOUSE_IN);
            }
        } else { // outside action.
            if (mouseHit()) { // entered action.
                mouseEntered(mouse);
                mouseFrag = fragSetTrue(mouseFrag, MOUSE_IN);
            }
        }

        components.forEach(MouseActionInvoker::invokeMouseAction);
    }

    public void setBackground(Color color) {
        this.background.set(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f
        );
    }

    public void setBackground(Vector4f color) {
        this.background.set(color);
    }

    public void setName(String name) {
        this.name = name;
    }
}
