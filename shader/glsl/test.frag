#version 120

uniform sampler2D sampler;
uniform vec2 r;
uniform float time;
uniform int key;
uniform vec4 background;

varying vec4 tex_colors;
varying vec2 tex_coods;
varying vec3 tex_normal;

void main(void){
    vec4 color = tex_colors;
    //    vec4 color = texture2D(sampler, tex_coods);

    gl_FragColor = color;
}