    - Como compilar
        - javac atividade01/item1/*.java

    - Como executar
        Para iniciar o servidor
        - java atividade01.TCPServer
        Para iniciar o cliente
        - java atividade01.TCPClient

    - Bibliotecas usadas (descrever as não padrões)
        Buffers de entrada e saída
        - DataInputStream
        - DataOutputStream

        Sockets e threads
        - InetAddress
        - Socket

        SHA-512
        - MessageDigest
        - BigInteger

        Array
        - ArrayList
        - List

    - Exemplo de uso
        - CONNECT admin,1234
        - PWD
        - CHDIR teste
        - GETDIRS
        - CHDIR teste
        - GETFILES 