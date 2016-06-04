
SepararModulos.sh "../DEG_Seriated.txt" "../DEG_AML_Borders.txt" "#" "Module"

cat "Top025_Module07.txt" "Top025_Module08.txt" "Top025_Module03.txt" "Top025_Module19.txt" > "Top100_SeriatedDEG.txt"

ConverterGenes.sh Hs ALIAS2EG "Top100_SeriatedDEG.txt" "Top100_SeriatedDEG_Entrez.txt"
ConverterGenes.sh Hs ALIAS2EG "Top100_GeneralRanking.txt" "Top100_GeneralRanking_Entrez.txt"

Enriquecer.sh Hs BP "GeneOntology_BP.txt" "Top100_SeriatedDEG_Entrez.txt" "Top100_GeneralRanking_Entrez.txt"
Enriquecer.sh Hs MF "GeneOntology_MF.txt" "Top100_SeriatedDEG_Entrez.txt" "Top100_GeneralRanking_Entrez.txt"
Enriquecer.sh Hs CC "GeneOntology_CC.txt" "Top100_SeriatedDEG_Entrez.txt" "Top100_GeneralRanking_Entrez.txt"
