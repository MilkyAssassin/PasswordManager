<script lang="ts">
    import { getContext } from "svelte";
    import type { Context, Entry } from "@my-types/types";
    import SoloEntry from "../SoloEntry.svelte";
    import tippy from "tippy.js";
    import { notifications } from "../Toast/notifications.svelte";
    import { deletePassword, editPassword } from "../../fetchData.svelte";

    type sortType =
        | "AlphabeticalOrder"
        | "AlphabeticalOrderReverse"
        | "DateCreated"
        | "DateCreatedReverse";
    type props = {
        search: string;
        sort: sortType;
    };

    let { search = $bindable(), sort = $bindable() }: props = $props();
    let context: Context = getContext("context");
    let vault = $derived(
        (context.user?.vault || []).filter(filter).sort(compareFn)
    );
    function filter(e: Entry) {
        if (e.title.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
            return true;
        if (e.notes.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
            return true;
        if (e.url.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
            return true;
        if (e.username.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
            return true;
        return false;
    }

    function compareFn(e1: Entry, e2: Entry): number {
        switch (sort) {
            case "AlphabeticalOrder":
                return e1.title.localeCompare(e2.title);
            case "AlphabeticalOrderReverse":
                return e2.title.localeCompare(e1.title);
            case "DateCreated":
                return e1.dateCreated.getTime() - e2.dateCreated.getTime();
            case "DateCreatedReverse":
                return e2.dateCreated.getTime() - e1.dateCreated.getTime();
        }
    }

    function tooltip(node: Element, fn: () => {}) {
        $effect(() => {
            const tooltip = tippy(node, fn());

            return tooltip.destroy;
        });
    }

    let selectedEntry: Entry | null = $state(null);
</script>

<div>
    {#if context.isLoading}
        Loading...
    {:else if 0 < vault.length}
        {#each vault as entry}
            <div
                class="flex justify-between px-10 w-full bg-[#fef7ff] hover:bg-white"
            >
                <button
                    onclick={() => {
                        if (entry.password && entry.password != "") {
                            navigator.clipboard.writeText(entry.password);
                            notifications.success("Password Copied!!!", 1000);
                        }
                    }}
                    class="w-full h-full py-4"
                    use:tooltip={() => ({
                        content: "Copy password",
                        hideOnClick: false,
                    })}
                >
                    <div class="flex gap-4">
                        <div
                            class="rounded-full bg-[#d8cbea] text-[#625282] h-7 w-7 text-center align-middle leading-7"
                        >
                            {entry.username[0]}
                        </div>
                        {entry.title}
                    </div>
                </button>
                <button
                    onclick={() => (selectedEntry = entry)}
                    class="py-4"
                    use:tooltip={() => ({
                        content: "Edit entry",
                        hideOnClick: false,
                    })}
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="w-7 h-7"
                        ><title>pencil</title><path
                            d="M19.71,8.04L17.37,10.37L13.62,6.62L15.96,4.29C16.35,3.9 17,3.9 17.37,4.29L19.71,6.63C20.1,7 20.1,7.65 19.71,8.04M3,17.25L13.06,7.18L16.81,10.93L6.75,21H3V17.25M16.62,5.04L15.08,6.58L17.42,8.92L18.96,7.38L16.62,5.04M15.36,11L13,8.64L4,17.66V20H6.34L15.36,11Z"
                        /></svg
                    >
                </button>
            </div>
        {/each}
    {:else}
        Your vault is empty, please add a password
    {/if}

    {#if selectedEntry !== null}
        <SoloEntry
            bind:selectedEntry
            close={(isNotChanged: boolean) => {
                if (
                    isNotChanged ||
                    window.confirm(
                        "Are you sure you want to close without saving your entry?"
                    )
                )
                    selectedEntry = null;
            }}
            delete_entry={async (entry: Entry) => {
                if (await deletePassword({ passwordId: entry.id })) {
                    window.location.reload();
                } else {
                    notifications.danger("An unexpected error happened", 2000);
                }
            }}
            save={async (entry: Entry) => {
                if (await editPassword(entry)) {
                    window.location.reload();
                } else {
                    notifications.danger("An unexpected error happened", 2000);
                }
            }}
        />
    {/if}
</div>
