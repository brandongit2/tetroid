#version 400

layout (location = 0) in vec3 pos;

uniform mat4 lightProjection;
uniform mat4 lightView;
uniform mat4 modelMatrix;

void main() {
    gl_Position = lightProjection * lightView * modelMatrix * vec4(pos, 1.0);
}
