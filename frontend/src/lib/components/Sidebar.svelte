<script lang="ts">
    import type { Entry } from "@/types/types";
    import SoloEntry from "./SoloEntry.svelte";
    const onclick = () => {
        alert("Complete function");
    };
    let createNewEntry = $state(false);
    let entry: Entry | null = $state(null);
    $inspect(entry);
</script>

<div class="w-full bg-[#d9d9d9] h-full">
    <div class="sticky top-0">
        <button
            class="block"
            onclick={() => {
                createNewEntry = true;
            }}>Create a new password</button
        >
        <button class="block" {onclick}>Two Factor Authetication</button>
        <button class="block" {onclick}>Edit Password</button>
    </div>
</div>

{#if createNewEntry}
    <SoloEntry
        selectedEntry={entry}
        close={() => {
            if (
                window.confirm(
                    "Are you sure you want to close without saving your entry?"
                )
            )
                createNewEntry = false;
        }}
        save={() => {
            alert("Write save func");
        }}
    />
{/if}
