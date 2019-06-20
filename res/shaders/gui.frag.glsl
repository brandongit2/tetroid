#version 400

in vec2 texCoord;
in vec3 vertColor;

layout (location = 0) out vec4 fragColor;

uniform sampler2D textureTile;

uniform int   elementType;
uniform int   isTextured;
uniform vec3  textColor;
uniform float opacity;

/* Element types:
 * 0 - Rectangle
 * 1 - Text
 */

void main() {
    switch (elementType) {
        case 0: {
            if (isTextured == 0) {
                fragColor = vec4(vertColor, opacity);
            } else {
                vec4 textured = texture(textureTile, texCoord);
                fragColor = vec4(textured.xyz, textured.w * opacity);
            }
            break;
        }
        case 1: {
            vec4 sampled = vec4(1.0, 1.0, 1.0, texture(textureTile, texCoord).r);
            fragColor = vec4(textColor, 1.0) * sampled;
            break;
        }
    }
}
