#---------------------------------------------------------------
# INPUT FILES

# Expressions.txt
#   FROM Macrae T, Sargeant T, Lemieux S, Hébert J et al.
#        RNA-Seq reveals spliceosome and proteasome genes as
#        most consistent transcripts in human cancer cells.
#        PLoS One 2013;8(9):e72884.
#        http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=GSE48173

# Network.txt
#   FROM Rolland T, Taşan M, Charloteaux B, et al.
#        A proteome-scale map of the human interactome network.
#        Cell. 2014;159(5):1212-1226. doi:10.1016/j.cell.2014.10.050.
#        http://interactome.dfci.harvard.edu/H_sapiens/

# EnsemblDB.txt
#   FROM http://www.ensembl.org/biomart/martview/

# Dendrograms.R
# DEG_Seriated.R

# Expressions_Labels.txt
# DEG_Labels.txt
# Chart_Healthy_Labels.txt
# Chart_ALL_Labels.txt
# Chart_AML_Labels.txt
# Chart_DEG_Labels.txt

#---------------------------------------------------------------
# ADJACENCY MATRIX OF THE GENE NETWORK

MatrizAdjacencias.sh "Network.txt" nao tab
mv "Network.txt.matriz.csv" "Network.csv"
mv "Network.txt.nomes.txt" "Seriation_Initial_Genes.txt"

#---------------------------------------------------------------
# SERIATION OF THE GENES

Experimento.sh "*.csv" 3600 96 1 Cla
GerarImagensDeExperimento.sh .

cp "Network.csv.CLA[1].ordem.txt" "Seriation_Final_Sequence.txt"
ConverterNumerosParaNomes.sh "Seriation_Final_Sequence.txt" "Seriation_Initial_Genes.txt" > "Seriation_Final_Genes.txt"

Informacao.sh "Network.csv" "Seriation_Final_Sequence.txt" > "Information.txt"
GerarImagem.sh "Network.csv" "Seriation_Final_Sequence.txt" "Network.png"

#---------------------------------------------------------------
# TRANSLATION OF GENE NAMES

ConverterGenes.sh Hs ALIAS2EG "Seriation_Final_Genes.txt" "Seriation_Final_Genes_Entrez.txt"
TraduzirColuna.sh "Seriation_Final_Genes.txt" 1 "EnsemblDB.txt" 1 3 "#" > "Seriation_Final_Genes_Entrez2.txt"

#---------------------------------------------------------------
# WINDOW MODULARITY

ModularidadeJanela.sh "Network.csv" "Seriation_Final_Sequence.txt" 251 > "WindowModularity.txt"
ModularidadeDensidade.sh "Network.csv" "Seriation_Final_Sequence.txt" 60 > "DensityModularity.txt"
GerarGrafico.sh "WindowModularity.txt" area 600 000000 "WindowModularity.png"
GerarGrafico.sh "DensityModularity.txt" area 600 000000 "DensityModularity.png"
Fronteiras.sh "WindowModularity.txt" 50 4 > "WindowModularity_Borders.txt"
ColorirModulos.sh "WindowModularity.png" "WindowModularity_Colored.png" < "WindowModularity_Borders.txt"

#---------------------------------------------------------------
# QUALITY METRICS

Qualidade.sh "Network.txt" "Seriation_Final_Genes.txt" "WindowModularity_Borders.txt" "Silhouette,Dunn,Connectivity" "WindowModularity_Quality.txt"
Silhouette.sh "Network.txt" "Seriation_Final_Genes.txt" "WindowModularity_Borders.txt" "WindowModularity_Silhouette.txt"

#---------------------------------------------------------------
# FUNCTIONAL ENRICHMENT - WINDOW MODULARITY

mkdir WindowModularity_Enrichment
cd WindowModularity_Enrichment

SepararModulos.sh "../Seriation_Final_Genes_Entrez.txt" "../WindowModularity_Borders.txt" "#" "M"

files=""
for f in M*txt; do
    files="$files$f "
done

Enriquecer.sh Hs BP GeneOntology_BP.txt $files
Enriquecer.sh Hs MF GeneOntology_MF.txt $files
Enriquecer.sh Hs CC GeneOntology_CC.txt $files

Heatmap.sh GeneOntology_BP.txt M 50
Heatmap.sh GeneOntology_MF.txt M 50
Heatmap.sh GeneOntology_CC.txt M 50

cd ..

#---------------------------------------------------------------
# TRANSCRIPTOGRAMS OF THE PATIENTS

OrganizarTabela.sh "Seriation_Final_Genes.txt" "Expressions.txt" tab > "Expressions_Seriated.txt"

Transcriptograma.sh "Expressions_Seriated.txt" 251 > "Transcriptograms.txt"

GraficoTranscriptograma.sh "WindowModularity.txt" "DensityModularity.txt" "Transcriptograms.txt" "1-17" "Expressions_Labels.txt" "Chart_Healthy_Labels.txt" "Chart_Healthy.svg"
GraficoTranscriptograma.sh "WindowModularity.txt" "DensityModularity.txt" "Transcriptograms.txt" "18-29" "Expressions_Labels.txt" "Chart_ALL_Labels.txt" "Chart_ALL.svg"
GraficoTranscriptograma.sh "WindowModularity.txt" "DensityModularity.txt" "Transcriptograms.txt" "30-72" "Expressions_Labels.txt" "Chart_AML_Labels.txt" "Chart_AML.svg"

#---------------------------------------------------------------
# DENDROGRAMS OF THE PATIENTS

R -q --no-save -e "source('Dendrograms.R');"

#---------------------------------------------------------------
# DIFFERENTIALLY EXPRESSED GENES - DEG

DEG.sh "Transcriptograms.txt" "DEG_ALL.txt" "1-17" "18-29"
DEG.sh "Transcriptograms.txt" "DEG_AML.txt" "1-17" "30-72"

#---------------------------------------------------------------
# HEATMAP OF THE DEG

HeatmapDEG.sh "Transcriptograms.txt" "Expressions_Labels.txt" "DEG_ALL.txt" "DEG_ALL.png"
HeatmapDEG.sh "Transcriptograms.txt" "Expressions_Labels.txt" "DEG_AML.txt" "DEG_AML.png"

#---------------------------------------------------------------
# SERIATION OF THE DEG

R -q --no-save -e "source('DEG_Seriated.R');"

Normalizar.sh "DEG_Seriated_ALL_Values.txt" virg 1000 > "DEG_Seriated_ALL_Values_Norm.txt"
Normalizar.sh "DEG_Seriated_AML_Values.txt" virg 1000 > "DEG_Seriated_AML_Values_Norm.txt"

#---------------------------------------------------------------
# DIFFERENTIALLY EXPRESSED MODULES

GerarGrafico.sh "DEG_Seriated_ALL_Values_Norm.txt" area 600 000000 "DEG_Seriated_ALL.png"
FronteirasDEG.sh "Seriation_Final_Genes.txt" "DEG_ALL.txt" S 4 2 S DESC 50 1000 "DEG_ALL_Borders.txt" "DEG_ALL_Modules.txt"
ColorirModulos.sh "DEG_Seriated_ALL.png" "DEG_Seriated_ALL_Colored.png" < "DEG_ALL_Borders.txt"

GerarGrafico.sh "DEG_Seriated_AML_Values_Norm.txt" area 600 000000 "DEG_Seriated_AML.png"
FronteirasDEG.sh "Seriation_Final_Genes.txt" "DEG_AML.txt" S 4 2 S DESC 50 1000 "DEG_AML_Borders.txt" "DEG_AML_Modules.txt"
ColorirModulos.sh "DEG_Seriated_AML.png" "DEG_Seriated_AML_Colored.png" < "DEG_AML_Borders.txt"

Transcriptograma.sh "DEG_Seriated.txt" 251 > "Transcriptograms_DEG.txt"
GraficoTranscriptograma.sh "WindowModularity.txt" "DensityModularity.txt" "Transcriptograms_DEG.txt" "0" "DEG_Labels.txt" "Chart_DEG_Labels.txt" "Chart_DEG.svg"

#---------------------------------------------------------------
# FUNCTIONAL ENRICHMENT - DEG ALL/HEALTHY MODULES

mkdir DEG_ALL_Enrichment
cd DEG_ALL_Enrichment

SepararModulos.sh "../Seriation_Final_Genes_Entrez.txt" "../DEG_ALL_Borders.txt" "#" "M"

files=""
for f in M*txt; do
    files="$files$f "
done

Enriquecer.sh Hs BP GeneOntology_BP.txt $files
Enriquecer.sh Hs MF GeneOntology_MF.txt $files
Enriquecer.sh Hs CC GeneOntology_CC.txt $files

Heatmap.sh GeneOntology_BP.txt M 50
Heatmap.sh GeneOntology_MF.txt M 50
Heatmap.sh GeneOntology_CC.txt M 50

cd ..

#---------------------------------------------------------------
# FUNCTIONAL ENRICHMENT - DEG AML/HEALTHY MODULES

mkdir DEG_AML_Enrichment
cd DEG_AML_Enrichment

SepararModulos.sh "../Seriation_Final_Genes_Entrez.txt" "../DEG_AML_Borders.txt" "#" "M"

files=""
for f in M*txt; do
    files="$files$f "
done

Enriquecer.sh Hs BP "GeneOntology_BP.txt" $files
Enriquecer.sh Hs MF "GeneOntology_MF.txt" $files
Enriquecer.sh Hs CC "GeneOntology_CC.txt" $files

Heatmap.sh "GeneOntology_BP.txt" M 50
Heatmap.sh "GeneOntology_MF.txt" M 50
Heatmap.sh "GeneOntology_CC.txt" M 50

cd ..

#---------------------------------------------------------------
# AVERAGE EXPRESSION PER PATIENT GROUP

MediaPerfis.sh "Expressions_Seriated.txt" "1-17" "18-29" "30-72" > "Expressions_Average_Patients.txt"

Transcriptograma.sh "Expressions_Average_Patients.txt" 251 > "Transcriptograms_Average_Patients.txt"
GraficoTranscriptograma.sh "WindowModularity.txt" "DensityModularity.txt" "Transcriptograms_Average_Patients.txt" "0" "Expressions_Average_Patients_Labels.txt" "Chart_Average_Patients_Labels.txt" "Chart_Average_Patients.svg"

#---------------------------------------------------------------
# AVERAGE EXPRESSION PER MODULE

MediaModulos.sh "Expressions_Average_Patients.txt" "WindowModularity_Borders.txt" > "Expressions_Average_Patients_WindowModules.txt"
MediaModulos.sh "Expressions_Average_Patients.txt" "DEG_ALL_Borders.txt" > "Expressions_Average_Patients_ALLModules.txt"
MediaModulos.sh "Expressions_Average_Patients.txt" "DEG_AML_Borders.txt" > "Expressions_Average_Patients_AMLModules.txt"

#---------------------------------------------------------------