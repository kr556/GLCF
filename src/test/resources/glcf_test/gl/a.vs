#version 430 core

uniform mat4 world;
uniform float div;
uniform vec2 mPos;

in vec3 pos;
in vec4 color;

out vec4 vColor;
out vec3 vPos;

// test

void main() {
    vColor = color;

    gl_Position = vec4(pos, 1);
}
