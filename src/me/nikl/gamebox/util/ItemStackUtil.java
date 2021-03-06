package me.nikl.gamebox.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * Created by Niklas on 13.04.2017.
 *
 * Utility class for ItemStacks
 */
public class ItemStackUtil {

    public static ItemStack getItemStack(String matDataString){
        Material mat; short data;
        if(matDataString == null) return null;
        String[] obj = matDataString.split(":");

        if (obj.length == 2) {
            try {
                mat = Material.matchMaterial(obj[0]);
            } catch (Exception e) {
                return null; // material name doesn't exist
            }

            try {
                data = Short.valueOf(obj[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null; // data not a number
            }

            //noinspection deprecation
            if(mat == null) return null;
            ItemStack stack = new ItemStack(mat, 1);
            stack.setDurability(data);
            return stack;
        } else {
            try {
                mat = Material.matchMaterial(obj[0]);
            } catch (Exception e) {
                return null; // material name doesn't exist
            }
            //noinspection deprecation
            return (mat == null ? null : new ItemStack(mat, 1));
        }
    }


    public static ItemStack createBookWithText(List<String> text) {
        return createItemWithText(text, new MaterialData(Material.BOOK_AND_QUILL));
    }

    public static ItemStack createItemWithText(List<String> text, MaterialData materialData) {
        ItemStack helpItem = materialData.toItemStack(1);

        ItemMeta meta = helpItem.getItemMeta();
        if(text != null) {
            if(text.size() > 0)meta.setDisplayName(text.get(0));
            if(text.size() > 1){
                text.remove(0);
                meta.setLore(text);
            }
        }
        helpItem.setItemMeta(meta);
        return helpItem;
    }
}
