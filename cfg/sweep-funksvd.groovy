import org.grouplens.lenskit.iterative.IterationCount
import org.grouplens.lenskit.iterative.RegularizationTerm
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity
import org.lenskit.api.ItemScorer
import org.lenskit.baseline.BaselineScorer
import org.lenskit.baseline.ItemMeanRatingItemScorer
import org.lenskit.baseline.UserMeanBaseline
import org.lenskit.baseline.UserMeanItemScorer
import org.lenskit.knn.item.ItemSimilarity
import org.lenskit.mf.funksvd.FeatureCount
import org.lenskit.mf.funksvd.FunkSVDItemScorer

bind ItemScorer to FunkSVDItemScorer
bind ItemSimilarity to CosineVectorSimilarity
bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer
bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
set IterationCount to 125

for (k in [5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100]) {
    algorithm("FunkSVD") {
        attributes['NFeatures'] = k
        attributes['Regularization'] = 0.02
        set FeatureCount to k
        set RegularizationTerm to 0.02d
    }
    algorithm("FunkSVD") {
        attributes['NFeatures'] = k
        attributes['Regularization'] = 0.05
        set FeatureCount to k
        set RegularizationTerm to 0.05d
    }
}
