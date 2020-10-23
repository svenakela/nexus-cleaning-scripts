import org.sonatype.nexus.repository.storage.Component
import org.sonatype.nexus.repository.storage.Query
import org.sonatype.nexus.repository.storage.StorageFacet

def repoName = 'YOUR-REPOSITORY-NAME-HERE'
def artifactGroup = 'com.your.artifact.groupid'
def artifactName = 'artifact-id-goes-here'
def numToKeep = 10

def compInfo = { Component c -> "${c.group()}:${c.name()}:${c.version()}[${c.lastUpdated()}]}" }
def repo = repository.repositoryManager.get(repoName)
def StorageFacet storageFacet = repo.facet(StorageFacet)
def Iterable<Component> components

def tx = storageFacet.txSupplier().get()
try {
    tx.begin()
    components = tx.findComponents(
        Query.builder()
            .where('group').eq(artifactGroup)
            .and('name').eq(artifactName)
            .suffix('order by last_updated desc skip ' + numToKeep)
            .build(),
        [repo])
    tx.commit()
} catch (Exception e) {
    log.warn("Transaction failed {}", e.toString())
    tx.rollback()
} finally {
    tx.close()
}

log.info("About to delete ${components.flatten(compInfo)}")
tx2 = storageFacet.txSupplier().get()
try {
    tx2.begin()
    for(Component c : components) {
        log.info("deleting ${compInfo(c)}")
        tx2.deleteComponent(c)
    }
    tx2.commit()
} catch (Exception e) {
    log.warn("Transaction failed {}", e.toString())
    tx2.rollback()
} finally {
    tx2.close()
}

log.info("Cleaning task for $artifactGroup:$artifactName finished")
