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
@synthesize tileMap = _tileMap;

+(CCScene *) scene{
    CCScene* scene = [CCScene node];
    MapLayer* layer = [MapLayer node];
    layer.char1=[[[Character alloc] characterWithParentNode:layer spriteFile:@"trs-012.gif"] autorelease];
    [layer.char1.characterSelectDelegateArray addObject:layer];
    [scene addChild:layer];
    return scene;
}

-(id) init{
    self = [super init];
    if(self!=nil){
        self.tileMap = [CCTMXTiledMap tiledMapWithTMXFile:@"ccdream_map01.tmx"];
        [self addChild:self.tileMap z:-1 tag:1]; 
        CCTMXLayer* eventLayer = [self.tileMap layerNamed:@"GameEventLayer"]; 
        eventLayer.visible = YES;
    }
    return self;
}

- (void)selectCharacter:(Character*) character{
    
    Position* charPosi = [Position positionWithX:0 Y:0];
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
    
    CCArray* posiArray = [PositionUtil calcMoveOrbitarrayFromPosition:charPosi movement:7 mobility:moblitity mapTypeMetrix:mapTypeMetrix maxPosition:maxPosi];
    CCLOG(@"count=%i",[posiArray count]);
    
    for(int i=0;i<[posiArray count];i++){
        MoveOrbit* mo = [posiArray objectAtIndex:i];
        CCLOG(@"%@",[mo.position description]);
        CCSprite* redSprite = [[CCSprite alloc] init];
        [redSprite setColor:ccc3(255, 0, 0)];
        redSprite.opacity=100;
        redSprite.textureRect = CGRectMake(0, 0, MAP_POINT_SIZE, MAP_POINT_SIZE);
        
        redSprite.position = CGPointMake(mo.position.x*32+16, mo.position.y*32+16);
        [self addChild:redSprite];
    }
}
@end
