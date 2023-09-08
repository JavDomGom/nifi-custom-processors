# AppendToFile
This custom processor append the content of FlowFiles (as string) to the end of a file that previously exists.

## Video tutorial
* [Apache NiFi: Develop custom processor AppendToFile (Spanish audio)](https://youtu.be/WXxvAxEgxgQ)

## Testing
1. Create the following NiFi Flow.
   <br><img src="img/2023-09-08_17-06.png" alt="NiFi logo" width="774"/><br><br>

2. **GenerateFlowFile** processor configuration.
   <br><img src="img/2023-09-08_16-57.png" alt="NiFi logo" width="774"/><br><br>

3. **UpdateAttribute** processor configuration.
   <br><img src="img/2023-09-08_16-58.png" alt="NiFi logo" width="774"/><br><br>

4. **PutFile** processor configuration.
   <br><img src="img/2023-09-08_16-59.png" alt="NiFi logo" width="774"/><br><br>

5. **AppendToFile** custom processor configuration.
   <br><img src="img/2023-09-08_17-00.png" alt="NiFi logo" width="774"/><br><br>

6. When starting NiFi Flow you can see that the first time the **PutFile** processor will create the file and the FlowFile will come out through the "*success*" relationship.
   <br><img src="img/2023-09-08_16-42.png" alt="NiFi logo" width="774"/><br><br>

7. The second and subsequent times, **PutFile** will fail (that's how it is configured in properties) because the file to write (testFile.txt) already exists in destination path. This is not a problem, it is just what we need for the FlowFile to be redirected through the "*failure*" relationship to the custom processor **AppendTofile**.
   <br><img src="img/2023-09-08_16-44.png" alt="NiFi logo" width="774"/><br><br>

8. The three queued FlowFiles come out through the "*success*" relationship.
   <br><img src="img/2023-09-08_16-45.png" alt="NiFi logo" width="774"/><br><br>

9. Finally, inspect the destination file to check if it has added each FlowFile as a string at the end of the file.
   <br><img src="img/2023-09-08_16-49.png" alt="NiFi logo" width="459"/>

