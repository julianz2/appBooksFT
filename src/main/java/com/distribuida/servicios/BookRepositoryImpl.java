package com.distribuida.servicios;

import com.distribuida.db.Book;
import io.helidon.common.reactive.Single;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbRow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookRepositoryImpl implements BookRepository {

    @Inject DbClient dbClient;

    private static Book map(DbRow row) {
        var id = row.column(1).as(Integer.class);
        var isbn = row.column(2).as(String.class);
        var title = row.column(3).as(String.class);
        var author = row.column(4).as(String.class);
        var price = row.column(5).as(BigDecimal.class);

        return new Book( id, isbn, title, author, price.doubleValue() );
    }

    private static Optional<Book> mapOpt(Optional<DbRow> row) {
        if( row.isPresent() ) {
            return Optional.of( BookRepositoryImpl.map(row.get()) );
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            return dbClient.execute( exec->exec.createQuery("select * from books order by id asc").execute() )
                    .map( BookRepositoryImpl::map ).collectList()
                    .get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> findById(Integer id) {
        try {
            Single<Optional<Book>> ret = dbClient.execute(
                    exec->exec.get("select * from books where id=?", id)
                            .map( BookRepositoryImpl::mapOpt )
                            .defaultIfEmpty( Optional.empty() )
                    );

            return ret.get( );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Book book) {
        dbClient.execute(exec -> exec.insert("insert into books (isbn,title,author,price) values(?,?,?,?)",
                book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPrice() )
        ).thenAccept( count-> System.out.printf("Inserted %d records\n", count) );
    }

    @Override
    public void update(Book book) {
        dbClient.execute(exec -> exec.update( "update books set isbn=?,title=?,author=?,price=? where id=?" ,
                book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getId() )
        ).thenAccept( count-> System.out.printf("Updated %d records\n", count) );
    }

    @Override
    public void delete(Integer id) {
        dbClient.execute(exec -> exec.delete( "delete from books where id=?", id )
        ).thenAccept( count-> System.out.printf("Deleted %d records\n", count) );
    }
}
