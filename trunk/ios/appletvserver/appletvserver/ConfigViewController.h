//
//  ConfigViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-31.
//
//

#import <Three20UI/Three20UI.h>

@interface ConfigViewController : TTTableViewController<UITextFieldDelegate>{
    UITextField* _serverIPField;
}
@property (nonatomic, retain) UITextField *serverIPField;
@end
