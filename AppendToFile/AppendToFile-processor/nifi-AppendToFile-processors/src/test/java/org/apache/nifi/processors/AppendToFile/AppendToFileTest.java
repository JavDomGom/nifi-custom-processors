/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.processors.AppendToFile;

import org.apache.nifi.flowfile.attributes.CoreAttributes;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisabledOnOs(value = OS.WINDOWS, disabledReason = "Test only runs on Unix or GNU")
public class AppendToFileTest {

    public static final String TARGET_DIRECTORY = "target/append-to-file";
    private File targetDir;

    @BeforeEach
    public void prepDestDirectory() throws IOException {
        targetDir = new File(TARGET_DIRECTORY);
        if (!targetDir.exists()) {
            Files.createDirectories(targetDir.toPath());
            return;
        }

        targetDir.setReadable(true);

        deleteDirectoryContent(targetDir);
    }

    private void deleteDirectoryContent(File directory) throws IOException {
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectoryContent(file);
            }
            Files.delete(file.toPath());
        }
    }

    @Test
    public void testCreateDirectory() throws IOException {
        final TestRunner runner = TestRunners.newTestRunner(new AppendToFile());
        String newDir = targetDir.getAbsolutePath() + "/new-folder";
        String fileName = "targetFile.txt";

        runner.setProperty(AppendToFile.DIRECTORY, newDir);

        Map<String, String> attributes = new HashMap<>();
        attributes.put(CoreAttributes.FILENAME.key(), fileName);

        Files.createDirectories(Paths.get(newDir));

        Path filePath = Paths.get(newDir, fileName);
        Files.createFile(filePath);

        // First FlowFile
        runner.enqueue("Hello world 1!".getBytes(), attributes);
        runner.run();

        // Second FlowFile
        runner.enqueue("Hello world 2!".getBytes(), attributes);
        runner.run();

        runner.assertAllFlowFilesTransferred(AppendToFile.REL_SUCCESS, 2);

        Path targetPath = Paths.get(TARGET_DIRECTORY + "/new-folder/targetFile.txt");
        byte[] content = Files.readAllBytes(targetPath);

        assertEquals("Hello world 1!\nHello world 2!\n", new String(content));
    }

    @AfterEach
    public void cleanUp() throws IOException {
        deleteDirectoryContent(targetDir);
    }
}