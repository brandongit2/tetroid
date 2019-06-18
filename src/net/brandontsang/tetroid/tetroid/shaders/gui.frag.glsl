#version 400

in vec2 texCoord;
in vec3 vertColor;

layout (location = 0) out vec4 fragColor;

uniform sampler2D textureId;

uniform int   isTextured;
uniform vec3  textColor;
uniform float opacity;

void main() {
    if (isTextured == 0) {
        fragColor = vec4(vertColor, opacity);
    } else {
        vec4 sampled = vec4(1.0, 1.0, 1.0, texture(textureId, texCoord).r);
        fragColor = vec4(textColor, 1.0) * sampled;
    }
}
