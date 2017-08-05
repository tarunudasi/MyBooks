package debugbridge.mybooks.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import debugbridge.mybooks.Model.BookLists;
import debugbridge.mybooks.R;

public class BookDescription extends AppCompatActivity {

    private TextView book_description, contact_person, contact_number, book_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BookLists bookLists = (BookLists) getIntent().getSerializableExtra("data");
        getSupportActionBar().setTitle(bookLists.getTitle());

        ImageView imageView = (ImageView) findViewById(R.id.image_collapsing);
        Picasso.with(this)
                .load(bookLists.getImg())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        book_description = (TextView) findViewById(R.id.book_description);
        contact_person = (TextView) findViewById(R.id.contact_person);
        contact_number = (TextView) findViewById(R.id.contact_number);
        book_cost = (TextView) findViewById(R.id.book_cost);

        book_cost.setText(bookLists.getCost() + " /-");
        book_description.setText(bookLists.getDesc());
        contact_number.setText(bookLists.getContact_number());
        contact_person.setText(bookLists.getContact_person());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
