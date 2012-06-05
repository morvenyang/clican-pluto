//
//  MapLayer.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "MapLayer.h"


@implementation MapLayer

@synthesize char1 = _char1;
@synthesize selectedCharacter = _selectedCharacter;
@synthesize tileMap = _tileMap;
@synthesize movementArray = _movementArray;
@synthesize shadowSpriteArray = _shadowSpriteArray;

+(CCScene *) scene{
    CCScene* scene = [CCScene node];
    MapLayer* layer = [MapLayer node];
    layer.char1=[Character characterWithParentNode:layer spriteFile:@"trs-012.gif"];
    [layer.char1.characterSelectDelegateArray addObject:layer];
    [scene addChild:layer];
    return scene;
}

-(id) init{
    self = [super init];
    if(self!=nil){
        self.tileMap = [CCTMXTiledMap tiledMapWithTMXFile:@"ccdream_map01.tmx"];
        self.shadowSpriteArray = [CCArray array];
        [self addChild:self.tileMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [self.tileMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = YES;
        [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
    }
    return self;
}

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    if(self.selectedCharacter!=nil){
        if(self.movementArray!=nil&&[PositionUtil containsPosition:posi forArray:self.movementArray]){
            //选中的位置是可移动的位置
            Character* character = self.selectedCharacter;
            character.selected = NO;
            [self.movementArray removeAllObjects];
            [self cleanShadowSpriteArray];
            CCLOG(@"move to%@",posi.description);
            [character.characterSprite runAction: [CCMoveTo actionWithDuration:0.5 position:[posi toCenterCGPoint]]];
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
    Mobility* moblitity = [Mobility mobilityWithDefault];
    
    
    
    int gridx=self.tileMap.contentSizeInPixels.width/self.tileMap.tileSize.width;
    int gridy=self.tileMap.contentSizeInPixels.height/self.tileMap.tileSize.height;
    NSMutableDictionary* mapTypeMetrix = [[[NSMutableDictionary alloc] init] autorelease];
    for(int i=0;i<gridx;i++){
        for(int j=0;j<gridy;j++){
            int tileId=[[self.tileMap layerNamed:@"GameEventLayer"] tileGIDAt:ccp(i, j)];
            NSDictionary* dictionary=[self.tileMap propertiesForGID:tileId];
            if(dictionary){
                NSString* mapTypeStr = [dictionary objectForKey:@"type"];
                Position* position = [Position positionWithX:i Y:gridy-j-1];
                int mapType = [mapTypeStr intValue];
                if(mapType==0){
                    CCLOG(@"Found mapType==0");
                }else{
                     [mapTypeMetrix setValue:[NSNumber numberWithInt:mapType] forKey:[position description]];
                }
               
            }
        }
    }
    Position* maxPosi = [Position positionWithX:gridx Y:gridy];
    
    CCArray* posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:3 mobility:moblitity mapTypeMetrix:mapTypeMetrix maxPosition:maxPosi];
    CCLOG(@"count=%i",[posiArray count]);
    
    [self cleanShadowSpriteArray];
    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*32+16, mo.position.y*32+16);
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
