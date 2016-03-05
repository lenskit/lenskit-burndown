package org.lenskit.burndown

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.TaskAction

class FetchDataset extends ConventionTask {
    def Object cacheDir;
    def String srcUrl;
    def Object dataDir;

    FetchDataset() {
        conventionMapping.cacheDir = {
            "$project.buildDir/dlcache"
        }
        outputs.upToDateWhen {
            project.file(getDataDir()).isDirectory()
        }
    }

    String getBaseName() {
        def idx = srcUrl.lastIndexOf('/')
        return srcUrl.substring(idx + 1)
    }

    @TaskAction
    void download() {
        project.mkdir project.file(getCacheDir())
        project.mkdir project.file(getDataDir())
        ant.get(src: srcUrl,
                dest: "${getCacheDir()}/${baseName}",
                skipexisting: true)
        project.copy {
            from project.zipTree("${getCacheDir()}/${baseName}")
            into getDataDir()
            eachFile {
                // FIXME fix the renaming
                if (it.relativePath.isFile()) {
                    it.path = it.sourceName
                } else {
                    it.exclude()
                }
            }
        }
    }
}
