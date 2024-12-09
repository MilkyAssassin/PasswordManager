<script lang="ts">
    import { fly, fade } from "svelte/transition";
    import _ from "lodash";
    import type { Entry } from "@my-types/types";
    import tippy from "tippy.js";
    import { notifications } from "./Toast/notifications.svelte";

    type props = {
        selectedEntry: Entry;
        close: (isChanged: boolean) => any;
        delete_entry?: ((entry: Entry) => any) | undefined;
        save: (entry: Entry, oldWebsite: string) => any;
    };
    let {
        selectedEntry = $bindable(),
        close,
        save,
        delete_entry = undefined,
    }: props = $props();
    let entry = $state($state.snapshot(selectedEntry));
    let showPassword = $state(false);
    let isSameEntry = $derived(
        _.isEqual($state.snapshot(selectedEntry), $state.snapshot(entry))
    );

    function tooltip(node: Element, fn: () => {}) {
        $effect(() => {
            const tooltip = tippy(node, fn());

            return tooltip.destroy;
        });
    }
</script>

<div
    class="fixed h-4/5 bottom-0 overflow-y-auto left-28 right-28 bg-[#625282] z-50 p-5 rounded-t-2xl"
    transition:fly={{ y: 200, duration: 1000 }}
>
    <div class="absolute right-0 top-0">
        <button onclick={() => close(isSameEntry)}>
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                class="h-12 w-12"
                ><title>close</title><path
                    d="M19,6.41L17.59,5L12,10.59L6.41,5L5,6.41L10.59,12L5,17.59L6.41,19L12,13.41L17.59,19L19,17.59L13.41,12L19,6.41Z"
                    fill="white"
                /></svg
            >
        </button>
    </div>
    <h1 class="text-xl text-center text-white">Edit Entry</h1>
    <hr class="pb-1" />
    <div class="grid grid-cols-2 gap-8">
        <div>
            <label class="block text-lg" for="">Title:</label>
            <input
                class="w-full rounded px-2 h-10"
                type="text"
                bind:value={entry.title}
            />
        </div>
        <div>
            <label class="block text-lg" for="">Username:</label>
            <input
                class="w-full rounded px-2 h-10"
                type="text"
                bind:value={entry.username}
            />
        </div>
        <div>
            <!-- https://pictogrammers.com/library/mdi/icon/eye-off-outline/ -->
            <label class="block text-lg" for="">Password:</label>
            <div class="w-full flex h-10 bg-white gap-2 pr-2">
                <input
                    class="w-full rounded-s px-2 h-10"
                    type={!showPassword ? "password" : "text"}
                    bind:value={entry.password}
                />
                <button
                    onclick={() => {
                        if (entry.password && entry.password != "")
                            navigator.clipboard.writeText(entry.password);
                        notifications.success("Password Copied!!!", 1000);
                    }}
                    use:tooltip={() => ({ content: "Copy Password" })}
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-10"
                        ><title>Copy password</title><path
                            d="M19,21H8V7H19M19,5H8A2,2 0 0,0 6,7V21A2,2 0 0,0 8,23H19A2,2 0 0,0 21,21V7A2,2 0 0,0 19,5M16,1H4A2,2 0 0,0 2,3V17H4V3H16V1Z"
                        /></svg
                    >
                </button>

                <button
                    onclick={() => (showPassword = !showPassword)}
                    use:tooltip={() => ({
                        content: showPassword
                            ? "Hide password"
                            : "Show password",
                    })}
                >
                    {#if !showPassword}
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            class="h-10 top-0 right-0"
                            ><title>Show password</title><path
                                d="M2,5.27L3.28,4L20,20.72L18.73,22L15.65,18.92C14.5,19.3 13.28,19.5 12,19.5C7,19.5 2.73,16.39 1,12C1.69,10.24 2.79,8.69 4.19,7.46L2,5.27M12,9A3,3 0 0,1 15,12C15,12.35 14.94,12.69 14.83,13L11,9.17C11.31,9.06 11.65,9 12,9M12,4.5C17,4.5 21.27,7.61 23,12C22.18,14.08 20.79,15.88 19,17.19L17.58,15.76C18.94,14.82 20.06,13.54 20.82,12C19.17,8.64 15.76,6.5 12,6.5C10.91,6.5 9.84,6.68 8.84,7L7.3,5.47C8.74,4.85 10.33,4.5 12,4.5M3.18,12C4.83,15.36 8.24,17.5 12,17.5C12.69,17.5 13.37,17.43 14,17.29L11.72,15C10.29,14.85 9.15,13.71 9,12.28L5.6,8.87C4.61,9.72 3.78,10.78 3.18,12Z"
                            /></svg
                        >
                    {:else}
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            class="h-10 top-0 right-0"
                            ><title>Hide password</title><path
                                d="M12,9A3,3 0 0,1 15,12A3,3 0 0,1 12,15A3,3 0 0,1 9,12A3,3 0 0,1 12,9M12,4.5C17,4.5 21.27,7.61 23,12C21.27,16.39 17,19.5 12,19.5C7,19.5 2.73,16.39 1,12C2.73,7.61 7,4.5 12,4.5M3.18,12C4.83,15.36 8.24,17.5 12,17.5C15.76,17.5 19.17,15.36 20.82,12C19.17,8.64 15.76,6.5 12,6.5C8.24,6.5 4.83,8.64 3.18,12Z"
                            /></svg
                        >
                    {/if}
                </button>
            </div>
        </div>
        <div>
            <label class="block text-lg" for="">URL:</label>
            <input
                class="w-full rounded px-2 h-10"
                type="text"
                bind:value={entry.url}
            />
        </div>
        <div>
            <label class="block text-lg" for="">Date created:</label>
            <input
                class="w-full rounded px-2 h-10"
                type="text"
                disabled
                value={entry.dateCreated}
            />
        </div>
        <div>
            <label class="block text-lg" for="">Last used:</label>
            <input
                class="w-full rounded px-2 h-10"
                type="text"
                disabled
                value={entry.lastUsed}
            />
        </div>
    </div>
    <div class="w-full">
        <label class="block" for="">Notes:</label>
        <textarea
            class="block w-full h-52 rounded px-2"
            bind:value={entry.notes}
        >
        </textarea>
    </div>
    <div class="h-5"></div>
    <div class="flex gap-6">
        {#if delete_entry !== undefined}
            <button
                class="w-full h-10 bg-gray-600 rounded-lg text-red-600"
                onclick={() => delete_entry(entry)}>Delete</button
            >
        {/if}
        <button
            class={"w-full h-10  rounded-lg " +
                (isSameEntry
                    ? "!cursor-not-allowed text-gray-600 bg-[#d8cbea]"
                    : "bg-[#844b8c]")}
            onclick={() => {
                if (isSameEntry) {
                    notifications.warning(
                        "You have not updated any fields",
                        2000
                    );
                } else {
                    save(entry, selectedEntry.url);
                }
            }}>Save</button
        >
    </div>
</div>

<button
    class="fixed left-0 right-0 bottom-0 top-0 bg-black opacity-80"
    aria-label="Close opened modal"
    onclick={() => close(isSameEntry)}
    transition:fade={{ duration: 1000 }}
>
</button>
