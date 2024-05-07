import org.glcf.main.*;
import org.glcf.main.gui.MouseMapper;
import org.glcf.main.reflect.GLAttribute;
import org.linear.main.matrix.Matrix4f;
import org.linear.main.vector.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class WinMainTest extends WindowMain {
    static {
        registEntryPoint(new WinMainTest());
    }

    float div = 0.0f;
    float size = 1;
    Vector2d mpos = new Vector2d();
    Vector2d mdelta = new Vector2d();

    @Override
    public void wmain(String[] args, Window window) {
        window.set().show();
//        VAO v = getDounutVAO(1024, 0.4);
//
//        VFShader shader = getVFShader("d");
//        shader.compile(v);
//
//        Model m = new Model(v);
//        m.setShader(shader);
//        m.setDrawMode(GL11.GL_LINE_STRIP);
//        m.setRoot(window);
//        m.setShaderUniform((s, t) -> {
//            s.setUniformm("world", Matrix4f.DIAGONAL.clone()
//                    .scale(size)
//                    .rotate(mdelta.len(), new Vector4f((float) mdelta.y * 10, (float) -mdelta.x * 10, 1, 0)));
//            s.setUniform("div", div);
//            s.setUniformvf("mPos", (float) mpos.x, (float) mpos.y);
//        });
//
//        window.frameRun(w -> {
//            MouseMapper mm = w.getMouseMapper();
//            Vector2d md = mm.getScroll();
//
//            mpos = mm.getPos();
//
//            if (mm.scrolled()) {
//                div += (float) md.x * 2;
//                size += (float) (md.y * 0.2);
//            }
//
//            if (mm.getAction() == GLFW.GLFW_PRESS) {
//                mdelta.add(mm.getDeltaPos());
//            }
//        });
//
//        window.add(m);
//
//        shader.compile(v);
    }

    public static VFShader getVFShader(String shaderName) {
        StringBuilder v = new StringBuilder();
        StringBuilder f = new StringBuilder();
        try (BufferedReader vin = new BufferedReader(new InputStreamReader(Test000.class.getResourceAsStream("glcf_test/gl/" + shaderName + ".vs")));
             BufferedReader fin = new BufferedReader(new InputStreamReader(Test000.class.getResourceAsStream("glcf_test/gl/" + shaderName +".fs")))) {
            vin.lines().forEach(l -> v.append("\n").append(l));
            fin.lines().forEach(l -> f.append("\n").append(l));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new VFShader(v.toString(), f.toString());
    }

    public VAO getDounutVAO(final int polyAmount, double size) {
        VBO pos = VBO.create(VertexUtils.toArray(getDounutPos(polyAmount, size)));

        float[] cArr = new float[polyAmount * 4];
        Arrays.fill(cArr, 1f);
        VBO color = VBO.create(cArr);

        VAO vao = new VAO(2);
        vao.attach(new IBO(IntStream.range(0, polyAmount).toArray()));
        vao.attach(0, pos, new GLAttribute(3, GL11.GL_FLOAT, false, "pos"));
        vao.attach(1, color, new GLAttribute(4, GL11.GL_FLOAT, false, "color"));
        vao.push();
        return vao;
    }

    public Vector3f[] getDounutPos(final int polyAmount, double size) {
        Vector3f[] poses = new Vector3f[polyAmount];
        final double polyDistanceCir = (2 * PI) / polyAmount;
        final double areaMin = -PI;

        double cirXpos;
        double cirYpos;
        for (int i = 0; i < polyAmount; i++) {
            Vector3f pos = poses[i] = new Vector3f();
            cirXpos = (float) sin(areaMin + polyDistanceCir * i);
            cirYpos = (float) cos(areaMin + polyDistanceCir * i);
            pos.set((float) cirXpos, (float) cirYpos, 0);

            pos.mul(size);
        }

        return poses;
    }
}
