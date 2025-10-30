// java
package org.recipe_system.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Optional;

public class FilePersistence<T extends Serializable> {
    private static final Logger logger = LoggerFactory.getLogger(FilePersistence.class);

    private final Path path;

    // Se "file" for absoluto, usa como está; se for relativo, resolve em <user.dir>/data/<file>
    public FilePersistence(String file) {
        Path provided = Paths.get(file);
        if (provided.isAbsolute()) {
            this.path = provided.normalize();
        } else {
            Path base = Paths.get(System.getProperty("user.dir"), "data");
            this.path = base.resolve(provided).toAbsolutePath().normalize();
        }
        logger.info("FilePersistence path resolvido: {}", this.path);
    }

    public Optional<ArrayList<T>> readFromFile() {
        try {
            if (!Files.exists(path)) {
                logger.info("Arquivo inexistente: {}", path);
                return Optional.empty();
            }
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
                @SuppressWarnings("unchecked")
                ArrayList<T> data = (ArrayList<T>) in.readObject();
                logger.info("Lido com sucesso de {} (qtd={})", path, data.size());
                return Optional.ofNullable(data);
            }
        } catch (Exception e) {
            logger.error("Erro ao ler de {}: {}", path, e.toString(), e);
            return Optional.empty();
        }
    }

    public void saveToFile(ArrayList<T> list) {
        Path parent = path.getParent();
        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                out.writeObject(list);
                out.flush();
            }
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            logger.info("Salvo com sucesso em {} (qtd={})", path, list.size());
        } catch (Exception e) {
            logger.error("Erro ao salvar em {}: {}", path, e.toString(), e);
            try {
                Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
                Files.deleteIfExists(tmp);
            } catch (IOException ignore) {}
            throw new RuntimeException("Falha ao salvar em " + path, e);
        }
    }

    public Path getResolvedPath() {
        return path;
    }
}
