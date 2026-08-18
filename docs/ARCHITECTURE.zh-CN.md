# TestPlugin_A 工程架构

## 目录结构

```text
com.testPlugin_A
├─ data/                         旧游戏 A 数据
├─ gameb/                        生命之树独立领域模块
│  ├─ gui/                       生命之树菜单
│  ├─ GameBService               状态流转、经济、偷取规则
│  └─ GameBStorage               gameB_data.json
├─ minigames/                    新 GUI 小游戏平台
│  ├─ api/
│  │  └─ GuiGame                 所有 GUI 游戏的统一接口
│  ├─ core/
│  │  ├─ ArcadeManager           游戏注册、总菜单、事件路由、定时刷新
│  │  └─ GuiToolkit              GUI 物品和 PDC 动作标签
│  ├─ data/
│  │  ├─ ArcadeProfile           玩家五个游戏的总档案
│  │  ├─ ArcadeRepository        UUID -> 档案
│  │  └─ ArcadeStorage           arcade_data.json
│  └─ games/
│     ├─ pet/                    宠物养成
│     ├─ snake/                  贪吃蛇
│     ├─ miner/                  矿工挖矿
│     ├─ fishing/                钓鱼大师
│     └─ alchemy/                炼金工坊
└─ main/
   ├─ Main                       只负责组装和生命周期
   ├─ TestCommand                指令入口
   ├─ listenerLogic/             旧功能监听器
   └─ packs/                     旧功能物品/菜单工厂
```

## 调用关系

```text
/testCommand arcade
        │
        ▼
ArcadeManager ──注册──> GuiGame 实现
        │                    │
        ├─读取 PDC 动作───────┤
        ├─每 5 tick 调用 tick │
        └─ArcadeRepository <──┘
                    │
                    ▼
              ArcadeStorage
              arcade_data.json
```

## 扩展新游戏

1. 在 `minigames/games/<游戏名>/` 创建实现 `GuiGame` 的类。
2. 使用 `GuiToolkit.button(...)` 给按钮写入游戏 ID 和动作 ID。
3. 在 `ArcadeManager` 构造器中调用一次 `register(...)`。
4. 需要持久化时，在 `ArcadeProfile` 增加该游戏的数据对象。

小游戏不得直接写其他 GUI 的槽位，也不应自行注册全局点击监听器。所有点击统一由
`ArcadeManager` 根据 PDC 标签路由，避免材质重复、标题误判和多个监听器互相冲突。

## 数据文件

- `gameA_data.json`：旧曲奇游戏。
- `gameB_data.json`：生命之树。
- `arcade_data.json`：五个新 GUI 小游戏。

三个文件彼此独立，任何新游戏的数据迁移都不会破坏旧游戏存档。
