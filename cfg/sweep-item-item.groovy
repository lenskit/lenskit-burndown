import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.ItemVectorNormalizer
import org.grouplens.lenskit.transform.normalize.MeanCenteringVectorNormalizer
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.VectorNormalizer
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity
import org.lenskit.api.ItemScorer
import org.lenskit.baseline.BaselineScorer
import org.lenskit.baseline.ItemMeanRatingItemScorer
import org.lenskit.baseline.UserMeanBaseline
import org.lenskit.baseline.UserMeanItemScorer
import org.lenskit.knn.NeighborhoodSize
import org.lenskit.knn.item.ItemItemScorer
import org.lenskit.knn.item.ItemSimilarity
import org.lenskit.knn.item.ModelSize
import org.lenskit.knn.item.model.ItemItemBuildContext
import org.lenskit.knn.item.model.ItemwiseBuildContextProvider

bind ItemScorer to ItemItemScorer
within (ItemSimilarity) {
    bind VectorSimilarity to CosineVectorSimilarity
}
bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer
set ModelSize to 2000

for (n in [5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100]) {
    algorithm("ItemItem") {
        attributes['NNbrs'] = n
        attributes['Normalization'] = 'User'
        set NeighborhoodSize to n
        within (UserVectorNormalizer) {
            bind VectorNormalizer to MeanCenteringVectorNormalizer
        }
    }
    algorithm("ItemItem") {
        attributes['NNbrs'] = n
        attributes['Normalization'] = 'Item'
        set NeighborhoodSize to n
        bind ItemItemBuildContext toProvider ItemwiseBuildContextProvider
        within (ItemVectorNormalizer) {
            bind VectorNormalizer to MeanCenteringVectorNormalizer
        }
        bind UserVectorNormalizer to BaselineSubtractingUserVectorNormalizer
        within (UserVectorNormalizer) {
            bind (BaselineScorer, ItemScorer) to ItemMeanRatingItemScorer
        }
    }
}
