#version 400 core

const int MAX_LIGHTS = 3;

in vec3 fragPos; // The position on the mesh represented by the current fragment.
in vec4 lightSpaceFragPos; // The position of the fragment in light space.
in vec2 texCoord;
in vec3 u_normal;

layout (location = 0) out vec4 fragColor;

uniform sampler2D textureTile;
uniform sampler2D depthMap;

uniform mat4  modelMatrix;
uniform int   matId;
uniform vec3  color;
uniform int   isTextured;
uniform float opacity;
uniform vec3  ambient;
uniform vec3  cameraPos;
uniform int   lightType[MAX_LIGHTS];
uniform vec3  lightPos[MAX_LIGHTS];
uniform vec3  lightDir[MAX_LIGHTS];
uniform vec3  lightColor[MAX_LIGHTS];

uniform float reflectivity;
uniform float shininess;

/* Material IDs
 * 0 - Plain
 * 1 - Phong
 */

/* Light types
 * 0 - Point
 * 1 - Sun
 */

void main() {
    switch (matId) {
        case 0: {
            if (isTextured == 0) {
                fragColor = vec4(color, opacity);
            } else {
                vec4 textured = texture(textureTile, texCoord);
                fragColor = vec4(textured.xyz, textured.w * opacity);
            }
            break;
        }
        case 1: {
            vec3 pos = (modelMatrix * vec4(fragPos, 1.0)).xyz;
            vec3 normal = normalize(u_normal);

            vec3 totalDiffuse = vec3(0.0, 0.0, 0.0);
            vec3 totalSpecular = vec3(0.0, 0.0, 0.0);
            for (int i = 0; i < MAX_LIGHTS; i++) {
                float attenuation;
                vec3 lightDirection;
                if (lightType[i] == 0) {
                    float lightDistance = length(lightPos[i] - pos);
                    attenuation = 1.0 / (1.0 + 0.1 * lightDistance + 0.01 * lightDistance * lightDistance);
                    lightDirection = normalize(lightPos[i] - pos);
                } else {
                    attenuation = 1.0;
                    lightDirection = -normalize(lightDir[i]);
                }

                // DIFFUSE LIGHTING
                // Diffuse lighting is based on the angle between the normal and `lightDir`.
                totalDiffuse += max(dot(normal, lightDirection), 0.0) * lightColor[i] * attenuation;

                // SPECULAR LIGHTING
                // Get the angle between the reflected ray and the view direction.
                vec3 viewDir = normalize(-cameraPos - pos);
                vec3 halfwayDir = normalize(viewDir + lightDirection);
                float val = pow(max(dot(halfwayDir, normal), 0.0), shininess);
                totalSpecular += val * reflectivity * lightColor[i] * attenuation;
            }

            // SHADOWS
            vec3 projCoords = lightSpaceFragPos.xyz / lightSpaceFragPos.w;
            // `projCoords` is now in the range [-1, 1]. However, we would
            // like it to be in the range [0, 1]:
            projCoords = projCoords * 0.5 + 0.5;
            float closestDepth = texture(depthMap, projCoords.xy).r;
            float currentDepth = projCoords.z;
            float bias = 0.0005;
            float shadow = currentDepth - bias > closestDepth ? 1.0 : 0.0;
            if (projCoords.z > 1.0) shadow = 0.0;

            if (isTextured == 0) {
                fragColor = vec4((totalDiffuse * (1.0 - shadow) + ambient) * color + totalSpecular * (1.0 - shadow), opacity);
            } else {
                vec4 textured = texture(textureTile, texCoord);
                fragColor = vec4((totalDiffuse * (1.0 - shadow) + ambient) * textured.xyz + totalSpecular * (1.0 - shadow), textured.w * opacity);
            }
            break;
        }
    }
}
