#version 120

uniform float time;
uniform mat3 m3in;
uniform mat4 m4in;
uniform float frag;
uniform int key;

attribute vec3 vertices;
attribute vec4 color;
attribute vec2 textures;
attribute vec3 normal;

varying vec4 tex_colors;
varying vec2 tex_coods;
varying vec3 tex_normal;

mat3 mat3d() {
    return mat3(1, 0, 0, 0, 1, 0, 0, 0, 1);
}

void main() {
    float r = color.x * time * 10;
    float g = color.y * time * 10;
    float b = color.z * time * 10;
    float a = color.w;
    tex_colors = vec4(vec3(sin(r * 0.4), sin(g * 0.5334), cos(b * 0.144)), a);
    gl_Position = vec4(vertices, 1) * m4in;
}