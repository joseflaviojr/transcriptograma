def genes = [:]
new File(args[0]).withReader{ entrada ->
    def linha
    while( ( linha = entrada.readLine() ) != null ){
        if( linha.isEmpty() ) continue
        def colunas = linha.split('\t')
        def gene    = colunas[0]
        if( genes.containsKey(gene) ) continue
        genes.put(gene, true)
        println linha
    }
}