
java -Xmx3g -cp gsea2-2.2.2.jar xtools.gsea.Gsea \
-res Expressions.txt -cls Phenotypes.cls#ALL_versus_HEALTHY -gmx c5.all.v5.1.symbols.gmt \
-out Results -rpt_label GSEA_ALL_HEALTHY \
-collapse false -mode Max_probe -norm meandiv -nperm 1000 -permute phenotype \
-rnd_type no_balance -scoring_scheme weighted \
-metric Signal2Noise -sort real -order descending -include_only_symbols true \
-make_sets true -median false -num 100 -plot_top_x 20 -rnd_seed timestamp \
-save_rnd_lists false -set_max 500 -set_min 15 -zip_report false -gui false

java -Xmx3g -cp gsea2-2.2.2.jar xtools.gsea.Gsea \
-res Expressions.txt -cls Phenotypes.cls#AML_versus_HEALTHY -gmx c5.all.v5.1.symbols.gmt \
-out Results -rpt_label GSEA_AML_HEALTHY \
-collapse false -mode Max_probe -norm meandiv -nperm 1000 -permute phenotype \
-rnd_type no_balance -scoring_scheme weighted \
-metric Signal2Noise -sort real -order descending -include_only_symbols true \
-make_sets true -median false -num 100 -plot_top_x 20 -rnd_seed timestamp \
-save_rnd_lists false -set_max 500 -set_min 15 -zip_report false -gui false
