package org.glcf2.models;

import org.glcf2.Drawing;
import org.glcf2.Shader;
import org.glcf2.Texture;
import org.glcf2.programobject.ProgramObjct;
import org.glcf2.vertex.ArrayModel;
import org.linear.main.matrix.Matrix4f;
import org.linear.main.vector.Vector4f;

public abstract class Model<T> implements Drawing, Cloneable {
    protected Shader prg;
    protected ArrayModel<Matrix4f, Vector4f> model;

    @Override
    public void setShader(Shader shader) {
        this.prg = shader;
    }

    @Override
    public Shader getShader() {
        return prg;
    }

    public void setPrgogram(Shader prg) {
        this.prg = prg;
    }

    public abstract void setVerties(T verties);

    public abstract void setTexture(Texture tex);

    public abstract void setColors(T colors);

    public abstract Texture getTexrure();

    @Override
    public Model<T> clone() {
        try {
            Model<T> re = (Model<T>) super.clone();
            if (prg != null)re.prg = this.prg.clone();
            if (model != null) re.model = this.model.clone();

            return re;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone model.");
        }
    }
}