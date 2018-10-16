#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 texCoord;

out vec2 texCoords;

uniform mat4 transform;

void main() {
    gl_Position = transform * vec4(vertices, 1);
    texCoords = texCoord;
}