<script lang="ts">
    import type { Entry } from "@/types/types";
    import SoloEntry from "./SoloEntry.svelte";
    import { addPassword } from "../fetchData.svelte";
    import { notifications } from "./Toast/notifications.svelte";

    let createNewEntry = $state(false);
    let entry: Entry = $state({
        id: -1,
        title: "",
        username: "",
        password: "",
        url: "",
        dateCreated: new Date(),
        notes: "",
        lastUsed: new Date(),
    });
    $inspect(entry);
</script>

<div class="w-full bg-[#d9d9d9] h-full">
    <div class="sticky top-0 pt-5">
        <button
            class="bg-white px-6 py-4 rounded-2xl flex mx-auto"
            onclick={() => {
                createNewEntry = true;
            }}
        >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                class="h-5"
                ><title>plus</title><path
                    d="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z"
                /></svg
            >
            <p class="leading-5">Create a new password</p>
        </button>
    </div>
</div>

{#if createNewEntry}
    <SoloEntry
        bind:selectedEntry={entry}
        close={(isNotChanged: boolean) => {
            if (
                isNotChanged ||
                window.confirm(
                    "Are you sure you want to close without saving your entry?"
                )
            )
                createNewEntry = false;
        }}
        save={async (entry: Entry, oldWebsite: string) => {
            if (await addPassword(entry)) {
                window.location.reload();
            } else {
                notifications.danger("An unexpected error happened", 2000);
            }
        }}
    />
{/if}
