package com.example.smartfridgelite;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products = new ArrayList<>();
    private final OnDeleteListener deleteListener;
    private final OnCartListener cartListener;
    private final OnEditListener editListener;

    public interface OnDeleteListener {
        void onDelete(Product product);
    }

    public interface OnCartListener {
        void onAddToCart(Product product);
    }
    public interface OnEditListener {
        void onEdit(Product product);
    }

    public ProductAdapter(OnDeleteListener deleteListener,
                          OnCartListener cartListener,
                          OnEditListener editListener) {
        this.deleteListener = deleteListener;
        this.cartListener = cartListener;
        this.editListener = editListener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = products.get(position);
        holder.tvName.setText(p.name);
        holder.tvCategory.setText(p.category + " · " + p.quantity + " unid.");

        long days = p.getDaysUntilExpiration();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = sdf.format(new Date(p.expirationDate));

        if (p.isExpired()) {
            holder.tvDays.setText("⚠️ Vencido el " + dateStr);
            holder.tvDays.setTextColor(Color.parseColor("#F44336"));
            holder.statusIndicator.setBackgroundColor(Color.parseColor("#F44336"));
        } else if (p.isExpiringSoon()) {
            holder.tvDays.setText("⏰ Vence en " + days + " día(s)-" + dateStr);
            holder.tvDays.setTextColor(Color.parseColor("#FF9800"));
            holder.statusIndicator.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {
            holder.tvDays.setText("✅ Vence el " + dateStr + " (" + days + " días)");
            holder.tvDays.setTextColor(Color.parseColor("#4CAF50"));
            holder.statusIndicator.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        holder.btnCart.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Lista de compras")
                    .setMessage("¿Agregar \"" + p.name + "\" a la lista de compras?")
                    .setPositiveButton("Sí, agregar", (dialog, which) ->
                            cartListener.onAddToCart(p))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(p));
        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(p));
    }

    @Override
    public int getItemCount() { return products.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvDays;
        View statusIndicator;
        ImageButton btnDelete, btnCart;
        ImageButton btnEdit;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvProductName);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvDays = v.findViewById(R.id.tvDaysLeft);
            statusIndicator = v.findViewById(R.id.statusIndicator);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnCart = v.findViewById(R.id.btnAddToCart);
            btnEdit = v.findViewById(R.id.btnEdit);
        }
    }
}