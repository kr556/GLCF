import org.glcf.main.VAO;
import org.glcf.main.*;
import org.glcf.main.Window;
import org.glcf.main.gui.GUIShader;
import org.glcf.util.ModelUtils;
import org.linear.main.matrix.Matrix4f;
import org.linear.main.vector.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Math.*;
import static org.glcf.main.VertexUtils.toArray;
import static org.linear.main.matrix.MatrixUtil.transformation;
import static org.lwjgl.opengl.GL20.*;

public class Test000 extends WindowMain {
    static {
        registEntryPoint(new Test000());
    }

    @Override
    public void wmain(String[] args, Window window) throws Exception{
        StringBuilder v = new StringBuilder();
        StringBuilder f = new StringBuilder();
        try (BufferedReader vin = new BufferedReader(new InputStreamReader(StaticTest.class.getResourceAsStream("glcf_test/gl/a.vs")));
             BufferedReader fin = new BufferedReader(new InputStreamReader(StaticTest.class.getResourceAsStream("glcf_test/gl/a.fs")))) {
            vin.lines().forEach(l -> v.append("\n").append(l));
            fin.lines().forEach(l -> f.append("\n").append(l));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

