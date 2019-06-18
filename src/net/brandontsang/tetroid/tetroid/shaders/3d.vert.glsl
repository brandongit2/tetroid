#version 400 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 v_texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 color;

out vec3 fragPos;
out vec2 texCoord;
out vec3 u_normal;
out vec3 vertColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(pos, 1.0);
    fragPos = pos;
    texCoord = v_texCoord;
    u_normal = normal;
    vertColor = color;
}
