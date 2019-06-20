#version 400

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 v_texCoord;
layout (location = 3) in vec3 color;

out vec2 texCoord;
out vec3 vertColor;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * modelMatrix * vec4(pos, 1.0);
    texCoord = v_texCoord;
    vertColor = color;
}
