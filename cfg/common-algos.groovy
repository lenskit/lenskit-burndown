import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.ItemVectorNormalizer
import org.grouplens.lenskit.transform.normalize.MeanCenteringVectorNormalizer
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.VectorNormalizer
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity
import org.grouplens.lenskit.vectors.similarity.VectorSimilarity
import org.lenskit.api.ItemScorer
import org.lenskit.baseline.*
import org.lenskit.knn.NeighborhoodSize
import org.lenskit.knn.item.ItemItemScorer
import org.lenskit.knn.item.ItemSimilarity
import org.lenskit.knn.item.model.ItemItemBuildContext
import org.lenskit.knn.item.model.ItemwiseBuildContextProvider
import org.lenskit.knn.user.UserSimilarity
import org.lenskit.knn.user.UserUserItemScorer
import org.lenskit.mf.funksvd.FunkSVDItemScorer

bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer

algorithm ("PersMean") {
    bind ItemScorer to UserMeanItemScorer
    set MeanDamping to 5
}
algorithm ("UserUser") {
    bind ItemScorer to UserUserItemScorer
    within (UserSimilarity) {
        bind VectorSimilarity to CosineVectorSimilarity
    }
    within (UserVectorNormalizer) {
        bind VectorNormalizer to MeanCenteringVectorNormalizer
    }
    set NeighborhoodSize to 30
}
algorithm ("ItemItem") {
    bind ItemScorer to ItemItemScorer
    within (ItemSimilarity) {
        bind VectorSimilarity to CosineVectorSimilarity
    }
    bind ItemItemBuildContext toProvider ItemwiseBuildContextProvider
    within (ItemVectorNormalizer) {
        bind VectorNormalizer to MeanCenteringVectorNormalizer
    }
    set NeighborhoodSize to 20
}
algorithm ("FunkSVD") {
    bind ItemScorer to FunkSVDItemScorer
    bind UserVectorNormalizer to BaselineSubtractingUserVectorNormalizer
}
