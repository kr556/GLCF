#version 430 core

uniform sampler2D samp;

in vec4 vColor;

out vec4 glColor;

void main() {
    glColor = vColor * vec4(1);
}