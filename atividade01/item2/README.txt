    - Como compilar
        - javac atividade01/item2/*.java

    - Como executar
        Para iniciar o servidor
        - java atividade01.TCPServer
        Para iniciar o cliente
        - java atividade01.TCPClient

    - Bibliotecas usadas (descrever as não padrões)
        Buffers de entrada e saída
        - DataInputStream
        - DataOutputStream
        - ByteArrayInputStream (Transmissão de dados via array de bytes)
        - ByteArrayOutputStream

        Arquivos
        - File
        - FileInputStream
        - FileOutputStream

        Sockets e threads
        - InetAddress
        - Socket

    - Exemplo de uso
        - ADDFILE teste.txt
        - GETFILESLIST
        - GETFILE teste2.txt
        - DELETE teste2.txt
        - DELETE teste.txt