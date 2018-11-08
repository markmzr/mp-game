#version 330 core

uniform sampler2D sampler;

in vec2 texCoords;

void main() {
    gl_FragColor = texture2D(sampler, texCoords);

    if (gl_FragColor.a < 0.00001)
        discard;
}