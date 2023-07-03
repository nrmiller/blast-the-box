#version 330 core

uniform sampler2DArray _texture;
uniform int frame_index;
uniform int frame_width;
uniform int frame_height;

in vec3 vertexColor;
in vec2 TexCoord;

out vec4 FragColor;

void main()
{
    vec4 texel = texture(_texture, vec3(TexCoord, frame_index));
    FragColor = texel;
}
