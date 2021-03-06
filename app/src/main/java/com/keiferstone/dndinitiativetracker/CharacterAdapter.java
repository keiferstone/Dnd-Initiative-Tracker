package com.keiferstone.dndinitiativetracker;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private List<Character> characters;
    private OnCharacterClickListener listener;

    CharacterAdapter(@NonNull List<Character> characters, OnCharacterClickListener listener) {
        this.characters = characters;
        this.listener = listener;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CharacterViewHolder holder, int position) {
        final Character character = getItem(position);
        holder.container.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCharacterClicked(character, holder.getAdapterPosition());
            }
        });
        holder.container.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onCharacterLongClicked(character, holder.getAdapterPosition());
            }
            return true;
        });
        holder.background.setBackgroundColor(getBackgroundColor(holder.itemView.getContext(), position));
        holder.background.setVisibility(character.isDead() ? View.GONE : View.VISIBLE);
        holder.name.setText(character.getName());
        holder.initiative.setText(String.valueOf(character.getInitiative()));
        holder.initiativeBreakdown.setText(getInitiativeBreakdown(holder.itemView.getContext(), character));
        holder.initiativeBreakdown.setVisibility(character.getModifier() == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.marker.setVisibility(character.isMarked() ? View.VISIBLE : View.INVISIBLE);
        holder.selectedOverlay.setVisibility(character.isSelected() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    private Character getItem(int position) {
        return characters.get(position);
    }

    @ColorInt
    private int getBackgroundColor(Context context, int position) {
        if (position % 2 == 0) {
            return ContextCompat.getColor(context, R.color.white);
        } else {
            return ContextCompat.getColor(context, R.color.grey);
        }
    }

    private String getInitiativeBreakdown(Context context, Character character) {
        return context.getString(R.string.initiative_breakdown,
                character.getD20(),
                character.getModifier() >= 0 ? "+" : "",
                character.getModifier());
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        View deleteIcon;
        View container;
        View background;
        TextView name;
        TextView initiative;
        TextView initiativeBreakdown;
        View marker;
        View selectedOverlay;

        CharacterViewHolder(View itemView) {
            super(itemView);
            deleteIcon = itemView.findViewById(R.id.skull_icon);
            container = itemView.findViewById(R.id.character_container);
            background = itemView.findViewById(R.id.background);
            name = itemView.findViewById(R.id.name);
            initiative = itemView.findViewById(R.id.initiative);
            initiativeBreakdown = itemView.findViewById(R.id.initiative_breakdown);
            marker = itemView.findViewById(R.id.marker);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
        }
    }

    interface OnCharacterClickListener {
        void onCharacterClicked(Character character, int position);
        void onCharacterLongClicked(Character character, int position);
    }
}
