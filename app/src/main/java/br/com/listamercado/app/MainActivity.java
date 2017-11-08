package br.com.listamercado.app;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list_View;
    EditText txt_produto;
    ProdutoAdapter adapter;
    View.OnClickListener click_ck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckBox ck =(CheckBox) view;

            int posicao = (int) ck.getTag();

            Produto produtoSelecionado = adapter.getItem(posicao);
            Produto produtoDB = Produto.findById(Produto.class,
                    produtoSelecionado.getId());


            if (ck.isChecked()){
                produtoDB.setAtivo(true);
                produtoDB.save();
                produtoSelecionado.setAtivo(true);


            }else {
                produtoDB.setAtivo(false);
                produtoDB.save();
                produtoSelecionado.setAtivo(false);



            }

            adapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_View=(ListView) findViewById(R.id.list_viel);
        txt_produto = (EditText) findViewById(R.id.txt_produto);

        //select no banco
        List<Produto> lstProduto = Produto.listAll(Produto.class);


        //criando adapter
        adapter = new ProdutoAdapter(this, lstProduto);

        //ligar o adapter
        list_View.setAdapter(adapter);



    }

    public  void  inserirItem (View view
    ){

        String produto = txt_produto.getText().toString();
        if (produto.isEmpty()) return;

        //criasndo abjeto
        Produto p =new Produto(produto, false);

        //adicionando na lista
        adapter.add(p);

        //adicionando no banco
        p.save();

        //limpar a caixinha
        txt_produto.setText(null);
    }

    // class do adapter
    private class ProdutoAdapter extends ArrayAdapter<Produto>{

        public ProdutoAdapter(Context ctx, List<Produto> items){

            super(ctx ,0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

           View v = convertView;
            if (v == null){
                v= LayoutInflater.from(getContext())
                        .inflate(R.layout.item_lista,null);

            }
            Produto item = getItem(position);

            TextView txt_item_produto = v.findViewById(R.id.txt_item_produto);
            CheckBox ck_item_produto= v.findViewById(R.id.ck_item_produto);

            txt_item_produto.setText(item.getNome());
            ck_item_produto.setChecked(item.isAtivo());

            //guarda a posiçao  do item
            ck_item_produto.setTag(position);

            ck_item_produto.setOnClickListener(click_ck);

            return v;
        }
    }
}
