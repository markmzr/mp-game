package realestateempire.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL30.*;

class Shader {

    private final int program;

    Shader() {
        program = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, readFile("VertexShader.vs"));
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, readFile("FragmentShader.fs"));
        glCompileShader(fragmentShader);

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "texCoord");
        glLinkProgram(program);
        glValidateProgram(program);
    }

    void use(Matrix4f transform) {
        glUseProgram(program);
        int location = glGetUniformLocation(program, "transform");
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        transform.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    private String readFile(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File("./Shaders/" + filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
