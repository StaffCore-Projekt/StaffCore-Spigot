package de.lacodev.rsystem.enums;

import de.lacodev.rsystem.utils.Data;
import org.bukkit.inventory.ItemStack;

public enum Heads {

    ARROW("MzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=",
            "arrow"),
    EVENT_CREATOR(
            "NTZhN2QyMTk1ZmY3Njc0YmJiMTJlMmY3NTc4YTJhNjNjNTRhOTgwZTY0NzQ0NDUwYWM2NjU2ZTA1YTc5MDQ5OSJ9fX0=",
            "event"),
    ACTION("MmZkMjUzYzRjNmQ2NmVkNjY5NGJlYzgxOGFhYzFiZTc1OTRhM2RkOGU1OTQzOGQwMWNiNzY3MzdmOTU5In19fQ==",
            "action"),
    OAK_ARROW_LEFT(
            "YmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==",
            "oak_arrow_left"),
    OAK_ARROW_RIGHT(
            "MTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19",
            "oak_arrow_right"),
    GLOBE("YjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0=",
            "globe"),
    WARNING(
            "OGQ5YjYzYzNiNzQ1ZjVkZjg1MTE3NTk3MmVlZmQ3N2VjYTUyNjlkNDg2N2M0ZWRhMTU5NGZmM2U2NjM0NjU4In19fQ==",
            "warn"),
    RELOAD(
            "ZDA4MzMyMWQyZDAzYjA1MWQ4NDhhM2MxY2E3NDQwMmU2NzJiYzk3MTk0MDFmNmRiOTIzMGI1NjFjMTdkYWMwZSJ9fX0=",
            "reload"),
    GERMANY(
            "NWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==",
            "de"),
    USA("NGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19",
            "us"),
    DENMARK(
            "MTBjMjMwNTVjMzkyNjA2ZjdlNTMxZGFhMjY3NmViZTJlMzQ4OTg4ODEwYzE1ZjE1ZGM1YjM3MzM5OTgyMzIifX19",
            "dk"),
    NETHERLAND(
            "YzIzY2YyMTBlZGVhMzk2ZjJmNWRmYmNlZDY5ODQ4NDM0ZjkzNDA0ZWVmZWFiZjU0YjIzYzA3M2IwOTBhZGYifX19",
            "nl"),
    RUSSIA(
            "MTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ==",
            "ru"),
    SPAIN(
            "MzJiZDQ1MjE5ODMzMDllMGFkNzZjMWVlMjk4NzQyODc5NTdlYzNkOTZmOGQ4ODkzMjRkYThjODg3ZTQ4NWVhOCJ9fX0=",
            "es"),
    FRANCE(
            "NTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ==",
            "fr"),
    ITALY(
            "ODVjZTg5MjIzZmE0MmZlMDZhZDY1ZDhkNDRjYTQxMmFlODk5YzgzMTMwOWQ2ODkyNGRmZTBkMTQyZmRiZWVhNCJ9fX0=",
            "it"),
    CZECH("NDgxNTJiNzMzNGQ3ZWNmMzM1ZTQ3YTRmMzVkZWZiZDJlYjY5NTdmYzdiZmU5NDIxMjY0MmQ2MmY0NmU2MWUifX19",
            "cz"),
    FINLAND(
            "NTlmMjM0OTcyOWE3ZWM4ZDRiMTQ3OGFkZmU1Y2E4YWY5NjQ3OWU5ODNmYmFkMjM4Y2NiZDgxNDA5YjRlZCJ9fX0=",
            "fi"),
    ESTONIA(
            "NDhjY2U2NjI3ZTRkYmE5NjY0MGRhN2JiY2E4Y2Q0M2E3ODM0OTUxNjdiYWYyODM0YWE5OTExNzAxOGFkZiJ9fX0=",
            "ee"),
    CROATIAN(
            "YjA1MGMwNGVjOGNhYmNlNzFkNzEwM2YzZTllZjRiYjg4MTlmOWYzNjVlYjMzNWE5MTM5OTEyYmMwN2VkNDQ1In19fQ==",
            "hr"),
    CHINESE(
            "N2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0=",
            "cn"),
    WEBUI(
            "MjM0N2EzOTQ5OWRlNDllMjRjODkyYjA5MjU2OTQzMjkyN2RlY2JiNzM5OWUxMTg0N2YzMTA0ZmRiMTY1YjZkYyJ9fX0=",
            "webui"),
    PLUS("MTBjOTdlNGI2OGFhYWFlODQ3MmUzNDFiMWQ4NzJiOTNiMzZkNGViNmVhODllY2VjMjZhNjZlNmM0ZTE3OCJ9fX0=",
            "plus"),
    REDD("ZWQ2OGFlMzE1MGQ4MWU0YzBhOWQxNzJiZDg0YzRmZjczY2RjMGI4N2ZlZThlYzY2MTIxMzQ2OGQ1NDQ0ODMifX19",
            "redd"),
    GRAYE(
            "MzBkNGYwYjVhMjFkYTY3MzY0MTM2M2JjNDY4ZTIwMWM0NzJjY2I5YmJmMTEyYWM1YjNjZjMxMzU4ZTMwY2Y0In19fQ==",
            "graye"),
    GRAYN(
            "ZTk2NzJlNzZlYjY5YWI0YjkyOTJmYTE2OWNjMmIxODllZjIzMTUxYmIzZWRlMjM4NDI4ZmM2YWJkZDA2MGY2In19fQ==",
            "grayn"),
    GRAYD(
            "MjdlOGFiYjY3ODZjZjBjOGI3ZjgzZGEzNmJmNWE0NTJlZGY1NGQyMGUyMzA5NjMyOThlYTc3YjhjM2YyZDAxNSJ9fX0=",
            "grayd"),
    GRAYB("MWU1YzY1MjI3Y2M0NmVhNDA1YjhhOGIxNWQ0YWM5ODg5NDQ2ZmU1ZDVmYjA2MTlmNTEzOThkZWIyNDExYSJ9fX0==",
            "grayb"),
    GRAYM("ZGRjZDhmZThkNGQ1YzA1ODg2ZGI5ZjU5MTI3OTYxNTQ5ZTYwMmYzMGM1MTc1OTk5ODEzMWIyYWQyNWQyNjQifX19",
            "graym"),
    REDB(
            "ZDRjNGEyNTRiZWM0NmUyNzQ3NTdjMDc5NzlmMGRkYWUzNmM3YjM2NTQ4N2M4ZjM1ZmZlYWQ0Y2IyZTMxMWI5In19fQ==",
            "redb"),
    REDM(
            "MTE2YWZiMzg1NzI0NzgzOTQ3YTYwNTJjYTI3NThmOWM5MzdjYjgxMzBjMGVhYzFhM2YwNjhkYzU1NDk1MzkzIn19fQ==",
            "redm");

    private final ItemStack item;
    private final String idTag;
    private final String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";

    Heads(String texture, String id) {

        item = Data.createSkull(prefix + texture, id);
        idTag = id;

    }

    public ItemStack getItemStack() {
        return item;
    }

    public String getName() {
        return idTag;
    }

}
