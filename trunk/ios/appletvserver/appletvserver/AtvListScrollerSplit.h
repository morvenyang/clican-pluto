//
//  ListScrollerSplit.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Foundation/Foundation.h>
#import "AtvHeader.h"
@interface AtvListScrollerSplit : NSObject{
    AtvHeader* _header;
    NSArray* _items;
}
@property (nonatomic, retain) AtvHeader* header;
@property (nonatomic, retain) NSArray* items;
@end
