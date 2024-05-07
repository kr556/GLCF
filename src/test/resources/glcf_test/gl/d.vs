#version 430 core

uniform mat4 world;
uniform float div;
uniform vec2 mPos;

in vec3 pos; in vec4 color;

out vec4 vColor;

vec2 cps(vec2 pos) {
    return vec2(sqrt(pos.x * pos.x + pos.y * pos.y), atan(pos.y, pos.x));
}

vec2 tps(vec2 pos) {
    return vec2(pos.x * cos(pos.y), pos.x * sin(pos.y));
}

void main() {
    vec3 p = pos;
    float ur = sin(atan(p.y, p.x) * div) * 0.06;
    float uz = cos(atan(p.y, p.x) * div) * 0.06;

    vec3 cp = vec3(cps(p.xy), p.z);
    cp.x += ur;
    cp.z += uz;

    vec4 wp = world * vec4(tps(cp.xy), cp.z, 1.0);

    float c = pow(1.1 - distance(wp.xy, mPos.xy), 5);
    vColor = vec4(c + 0.1, c, c + 0.2, 1);

    gl_Position = wp;
}
