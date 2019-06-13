#version 400 core

flat in vec3 vertNorm;

out vec4 FragColor;

void main() {
    FragColor = vec4(vertNorm, 1.0f);
}
