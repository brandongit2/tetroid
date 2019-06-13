#version 400 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 v_VertNorm;

flat out vec3 vertNorm;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(pos.x, pos.y, pos.z, 1.0);
    vertNorm = v_VertNorm;
}
