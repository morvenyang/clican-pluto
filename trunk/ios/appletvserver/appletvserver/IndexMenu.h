//
//  IndexMenu.h
//  appletvserver
//
//  Created by zhang wei on 13-4-16.
//
//

#import <Foundation/Foundation.h>

@interface IndexMenu : NSObject{
    NSString* _title;
    NSString* _onSelect;
}
@property (nonatomic, copy) NSString* title;
@property (nonatomic, copy) NSString* onSelect;
@end
