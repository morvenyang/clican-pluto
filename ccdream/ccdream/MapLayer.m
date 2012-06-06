//
//  MapLayer.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapLayer.h"


@implementation MapLayer

@synthesize playerCharacterArray = _playerCharacterArray;
@synthesize enemyCharacterArray = _enemyCharacterArray;
@synthesize selectedCharacter = _selectedCharacter;
@synthesize tiledMap = _tiledMap;
@synthesize movementArray = _movementArray;
@synthesize shadowSpriteArray = _shadowSpriteArray;
@synthesize maxPosi = _maxPosi;
@synthesize mapAttribute = _mapAttribute;
@synthesize mapGridAttributeMap = _mapGridAttributeMap;



-(id) init{
    self = [super init];
    if(self!=nil){
        
        //加载关卡地图
        self.tiledMap = [CCTMXTiledMap tiledMapWithTMXFile:@"ccdream_map01.tmx"];
        self.shadowSpriteArray = [CCArray array];
        [self addChild:self.tiledMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [self.tiledMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = YES;
        [self loadMap];
        //加载关卡玩家角色和敌对角色
        [self loadCharacter];
        
        [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
    }
    return self;
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
    
    for(int i=0;i<[self.mapAttribute.enemyBeginPosiArray count];i++){
        Position* position = [self.mapAttribute.enemyBeginPosiArray objectAtIndex:i];
        Character* character =[Character characterWithParentNode:self spriteFile:@"trs-037.gif" position:position];
        [character addCharacterSelectDelegate:self];
        [self.enemyCharacterArray addObject:character];
    }
   
    for(int i=0;i<[self.mapAttribute.playerBeginPosiArray count];i++){
        Position* position = [self.mapAttribute.playerBeginPosiArray objectAtIndex:i];
        Character* character =[Character characterWithParentNode:self spriteFile:@"trs-012.gif" position:position];
        [character addCharacterSelectDelegate:self];
        [self.playerCharacterArray addObject:character];
    }
}
- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    [[FightMenuLayer sharedFightMenuLayer] hide];
    if(self.selectedCharacter!=nil){
        if(self.movementArray!=nil&&[PositionUtil containsPosition:posi forMoveOrbitArray:self.movementArray]){
            //选中的位置是可移动的位置
            Character* character = self.selectedCharacter;
            character.selected = NO;
            [self.movementArray removeAllObjects];
            [self cleanShadowSpriteArray];
            CCLOG(@"move to%@",posi.description);
            
            [character.characterSprite runAction: [CCMoveTo actionWithDuration:0.5 position:[posi toCenterCGPoint]]];
            [[FightMenuLayer sharedFightMenuLayer] showAtPosition:[posi  toFightMenuPosition:self.maxPosi]];
            [self cleanShadowSpriteArray];
            return YES;
        }
    }
    self.selectedCharacter = nil;
    [self.movementArray removeAllObjects];
    [self cleanShadowSpriteArray];
    return NO;
}

- (void)selectCharacter:(Character*) character{
    //被选中了
    character.selected = YES;
    self.selectedCharacter = character;
    
    Position* charPosi = [Position positionWithCGPoint:self.selectedCharacter.characterSprite.position];
    [[FightMenuLayer sharedFightMenuLayer] showAtPosition:[charPosi toFightMenuPosition:self.maxPosi]];
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    CCArray* posiArray = nil;
    //计算可移动范围
    
    posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:3 mobility:moblitity mapGridAttributeMap:self.mapGridAttributeMap maxPosition:self.maxPosi playerCharacterArray:self.playerCharacterArray enemyCharacterArray:self.enemyCharacterArray];
    CCLOG(@"count=%i",[posiArray count]);
    
    [self cleanShadowSpriteArray];
    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*MAP_POINT_SIZE+MAP_POINT_SIZE/2, mo.position.y*MAP_POINT_SIZE+MAP_POINT_SIZE/2);
        [self.shadowSpriteArray addObject:redSprite];
        [self addChild:redSprite];
    }
    self.movementArray = posiArray;
}

-(void) cleanShadowSpriteArray{
    int count = [self.shadowSpriteArray count];
    for(int i=0;i<count;i++){
        CCSprite* shadowSprit = [self.shadowSpriteArray objectAtIndex:i];
        [shadowSprit removeFromParentAndCleanup:YES];
    }
}
@end
