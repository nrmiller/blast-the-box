#version 330 core
in vec3 vertexColor;
out vec4 FragColor;
in vec2 TexCoord;

uniform sampler2D _texture;

void main()
{
    vec4 texel = texture(_texture, TexCoord);
    FragColor = vec4(vertexColor.r, vertexColor.g, vertexColor.b, 1.0f);
}
