# We will use Vim cause we are the cool kids off course

<!--toc:start-->

- [We will use Vim cause we are the cool kids off course](#we-will-use-vim-cause-we-are-the-cool-kids-off-course)
  - [Installing java](#installing-java)
  <!--toc:end-->

So here is how we can setup the env:

## Installing java

1. SDK man will rescue you:

```bash
curl -s "https://get.sdkman.io" | bash
```

2. install java and gradle

```bash
sdk install java
sdk install gardle
```

3. You can use gradle to start a java app in an empty directory

```bash
gradle init
```

PS: for complete list of gradle tasks run `gradle tasks` to run the app run `gradle run`.

4. Now go to your neovim config and change the tree sitter insure installed and add java:

```lua
return {
  "nvim-treesitter/nvim-treesitter",
  opts = function(_, opts)
    opts.ensure_installed = require("astronvim.utils").list_insert_unique(opts.ensure_installed, {
      "lua",
      "c",
        -- add only the following line
      "java",
    })
  end,
}

```

5. Now open Mason and install `jdtls` the Java LSP
6. [optional] you can install this extension and configure like this in you `user.lua` or `init.lua`

```lua
{
    "mfussenegger/nvim-jdtls",
    config = function()
      require("nvim-jdtls").start_or_attach {
        cmd = { "jdtls" },
        root_dir = require("jdtls.setup").find_root { "gradle.build", "pom.xml", ".git" },
      }
    end,
  }

```

And Voila, You are done and probably you hate me and your life. So if you're a sane person just
use VS code with JAVA Pack or IntejiJ.
Note: the first 2 steps are essential
