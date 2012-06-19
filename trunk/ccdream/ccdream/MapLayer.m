//
//  MapLayer.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapLayer.h"
#import "EngineContext.h"
#import "EventDispatcher.h"
#import "PositionUtil.h"
@implementation MapLayer

@synthesize playerCharacterArray = _playerCharacterArray;
@synthesize enemyCharacterArray = _enemyCharacterArray;
@synthesize tiledMap = _tiledMap;
@synthesize movementArray = _movementArray;
@synthesize shadowSpriteArray = _shadowSpriteArray;
@synthesize maxPosi = _maxPosi;
@synthesize mapAttribute = _mapAttribute;
@synthesize mapGridAttributeMap = _mapGridAttributeMap;
@synthesize fightMapSession = _fightMapSession;
@synthesize mapMenu = _mapMenu;
@synthesize nextRound = _nextRound;
@synthesize nextAIAction = _nextAIAction;

static MapLayer* sharedMapLayer = nil;

+(MapLayer*) sharedMapLayer{
    @synchronized(self) {
        if (sharedMapLayer == nil){
            sharedMapLayer = [MapLayer node];
    }
    }
        return sharedMapLayer;
}

-(id) init{
    self = [super init];
    if(self!=nil){
        
        //加载关卡地图
        self.tiledMap = [CCTMXTiledMap tiledMapWithTMXFile:@"map01.tmx"];
        self.shadowSpriteArray = [CCArray array];
        [self addChild:self.tiledMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [self.tiledMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = YES;
        [self loadMap];
        //加载关卡玩家角色和敌对角色
        [self loadCharacter];
        
        [CCMenuItemFont setFontSize:20];
        self.nextAIAction = -1;
        self.nextRound = [CCMenuItemFont 
                                       itemFromString:@"下回合" target:self selector:@selector(nextRound:)];

        self.mapMenu = [CCMenu menuWithItems:self.nextRound,nil];
        self.mapMenu.position = ccp(450,300);
        [self.mapMenu alignItemsVertically];
        [self addChild:self.mapMenu];
        [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
    }
    return self;
}

-(void) onEnemyAIActionND:(id)sender data:(Character*)character {
    Session* enemyFightMapSession = [[EngineContext sharedEngineContext] newSession:@"ws_enemy_fight_map" forSponsor:@"character"];
    NSMutableDictionary* params = [[[NSMutableDictionary alloc] init] autorelease];
    [params setValue:character forKey:PARAM_SELECTED_CHARACTER];
    
    [[EventDispatcher sharedEventDispatcher] dispatch:enemyFightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_MAP_CHARACTER_ONCLICK forParameters:params];
}

-(void) scheduleUpdates
{
    [self schedule:@selector(executeNextAIAction:) interval:0.1f];
}
-(void) executeNextAIAction:(ccTime)delta
{
    int enemyCount = [self.enemyCharacterArray count];
    if(self.nextAIAction<enemyCount-1){
        if(self.nextAIAction==-1){
            self.nextAIAction = 0;
            Character* character = [self.enemyCharacterArray objectAtIndex:0];
            Session* enemyFightMapSession = [[EngineContext sharedEngineContext] newSession:@"ws_enemy_fight_map" forSponsor:@"character"];
            NSMutableDictionary* params = [[[NSMutableDictionary alloc] init] autorelease];
            [params setValue:character forKey:PARAM_SELECTED_CHARACTER];
            
            [[EventDispatcher sharedEventDispatcher] dispatch:enemyFightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_MAP_CHARACTER_ONCLICK forParameters:params];
        }else{
            Character* character = [self.enemyCharacterArray objectAtIndex:self.nextAIAction];
            CCLOG(@"nextAIAction:%i,finished=%d",self.nextAIAction,character.finished);
            if(character.finished){
                self.nextAIAction = self.nextAIAction+1;
                character = [self.enemyCharacterArray objectAtIndex:self.nextAIAction];
                Character* character = [self.enemyCharacterArray objectAtIndex:self.nextAIAction];
                Session* enemyFightMapSession = [[EngineContext sharedEngineContext] newSession:@"ws_enemy_fight_map" forSponsor:@"character"];
                NSMutableDictionary* params = [[[NSMutableDictionary alloc] init] autorelease];
                [params setValue:character forKey:PARAM_SELECTED_CHARACTER];
                
                [[EventDispatcher sharedEventDispatcher] dispatch:enemyFightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_MAP_CHARACTER_ONCLICK forParameters:params];
            }
        }
        
    }else{
        [self unschedule:@selector(executeNextAIAction:)];
        self.nextAIAction = -1;
        for (Character* character in self.enemyCharacterArray) {
            character.finished = NO;
            [character.characterSprite.texture initWithImage:character.sourceCharacterImage];
        }
    }
}

-(void) nextRound:(id) sender{
    CCLOG(@"next round");
    [self scheduleUpdates];
    for (Character* character in self.playerCharacterArray) {
        character.finished = NO;
        [character.characterSprite.texture initWithImage:character.sourceCharacterImage];
    }
    
}

-(void) loadMap{
    int gridx=self.tiledMap.contentSizeInPixels.width/self.tiledMap.tileSize.width;
    int gridy=self.tiledMap.contentSizeInPixels.height/self.tiledMap.tileSize.height;
    
    self.mapAttribute = [MapAttribute mapAttributeWith:self.tiledMap];
    
    self.mapGridAttributeMap = [[[NSMutableDictionary alloc] init] autorelease];
    for(int i=0;i<gridx;i++){
        for(int j=0;j<gridy;j++){
            int tileId=[[self.tiledMap layerNamed:@"GameEventLayer"] tileGIDAt:ccp(i, j)];
            NSDictionary* dictionary=[self.tiledMap propertiesForGID:tileId];
            if(dictionary){
                Position* position = [Position positionWithX:i Y:gridy-j-1];
                MapGridAttribute* mga = [MapGridAttribute mapGridAttribut:dictionary position:position];
                [self.mapGridAttributeMap setValue:mga forKey:position.description];
            }
            
        }
    }
    
    self.maxPosi = [Position positionWithX:gridx-1 Y:gridy-1];
    CCLOG(@"max position=%@",self.maxPosi.description);
}

-(void) loadCharacter{
    self.playerCharacterArray = [CCArray array];
    self.enemyCharacterArray = [CCArray array];
    NSString* a1[] = {@"tsqa01.gif",@"tsqa02.gif",@"tsqa03.gif",@"tsqa05.gif",@"tsqa06.gif"};
     NSString* a2[] = {@"tsqa18.gif",@"tsqa19.gif",@"tsqa20.gif",@"tsqa21.gif",@"tsqa24.gif"};
    for(int i=0;i<[self.mapAttribute.enemyBeginPosiArray count];i++){
        Position* position = [self.mapAttribute.enemyBeginPosiArray objectAtIndex:i];
        Character* character =[Character characterWithParentNode:self spriteFile:a2[i] position:position];
        character.player = NO;
        [self.enemyCharacterArray addObject:character];
    }
   
    for(int i=0;i<[self.mapAttribute.playerBeginPosiArray count];i++){
        Position* position = [self.mapAttribute.playerBeginPosiArray objectAtIndex:i];
        Character* character =[Character characterWithParentNode:self spriteFile:a1[i] position:position];
        character.player = YES;
        [self.playerCharacterArray addObject:character];
    }
}
- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    
    if(self.fightMapSession!=nil&&[self.fightMapSession.status isEqualToString:STATUS_ACTIVE]){
        //如果fightMapSession存在则走该工作流
        [[EventDispatcher sharedEventDispatcher] dispatch:self.fightMapSession.sessionId forState:0 mapPosition:posi];
    }else{
        //如果工作流不存在则判断是否需要创建工作流
        for (Character* character in self.playerCharacterArray) {
            if([PositionUtil isPosition:posi forNode:character.characterSprite]){
                //如果选中了角色并且该角色还未操作过
                if(!character.finished){
                    self.fightMapSession = [[EngineContext sharedEngineContext] newSession:@"ws_fight_map" forSponsor:@"character"];
                    NSMutableDictionary* params = [[[NSMutableDictionary alloc] init] autorelease];
                    [params setValue:character forKey:PARAM_SELECTED_CHARACTER];
                    [[EventDispatcher sharedEventDispatcher] dispatch:self.fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_MAP_CHARACTER_ONCLICK forParameters:params];
                }else{
                    //如果操作过了则显示别的内容
                    
                }
                return YES;
            }
        }
    }
    
    return YES;
}



-(void) cleanShadowSpriteArray{
    int count = [self.shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [self.shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}
@end
