#version 400 core

const int MAX_LIGHTS = 3;

in vec3 fragPos; // The position on the mesh represented by the current fragment.
in vec3 u_normal;
in vec3 vertColor;

layout (location = 0) out vec4 fragColor;

uniform mat4 modelMatrix;
uniform int  matId;
uniform vec3 ambient;
uniform vec3 cameraPos;
uniform vec3 lightPos[MAX_LIGHTS];
uniform vec3 lightColor[MAX_LIGHTS];

uniform float reflectivity;
uniform float shininess;

/* Material IDs
 * 0 - Plain
 * 1 - Phong
 */

void main() {
    switch (matId) {
        case 0: {
            fragColor = vec4(vertColor, 1.0);
            break;
        }
        case 1: {
            vec3 pos = (modelMatrix * vec4(fragPos, 1.0)).xyz;
            vec3 normal = normalize(u_normal);

            vec3 totalDiffuse = vec3(0.0, 0.0, 0.0);
            vec3 totalSpecular = vec3(0.0, 0.0, 0.0);
            for (int i = 0; i < MAX_LIGHTS; i++) {
                float lightDistance = length(lightPos[i] - pos);
                float attenuation = 1.0 / (1.0 + 0.1 * lightDistance + 0.01 * lightDistance * lightDistance);

                // DIFFUSE LIGHTING
                vec3 lightDir = normalize(lightPos[i] - pos);
                // Diffuse lighting is based on the angle between the normal and `lightDir`.
                totalDiffuse += max(dot(normal, lightDir), 0.0) * lightColor[i] * attenuation;

                // SPECULAR LIGHTING
                // Get the angle between the reflected ray and the view direction.
                vec3 viewDir = normalize(-cameraPos - pos);
                vec3 halfwayDir = normalize(viewDir + lightDir);

                float val = pow(max(dot(halfwayDir, normal), 0.0), shininess);
                totalSpecular += val * reflectivity * lightColor[i] * attenuation;
            }

            fragColor = vec4((totalDiffuse + ambient) * vertColor + totalSpecular, 1.0);
            break;
        }
    }
}
