#version 330 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 texCoord;

out vec3 vertexColor;
out vec2 TexCoord;

uniform mat4 projectionView;
uniform mat4 model;

void main()
{
    gl_Position = projectionView * model * vec4(pos.x, pos.y, pos.z, 1.0);
    vertexColor = color;
    TexCoord = texCoord;
}
